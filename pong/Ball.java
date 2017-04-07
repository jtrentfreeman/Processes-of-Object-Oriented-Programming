package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{
    public int x, y, width = 25, height = 25;
    public int motionX, motionY;
    public Random random;
    private Pong pong;
    //public int amountOfHits;
    
    public Ball(Pong pong)
    {
        this.pong = pong;
        this.random = new Random();
        spawn();
    }
    
    // Update the ball's movement
    public void update(Paddle paddle1, Paddle paddle2)
    {
        int speed = 5;

        this.x += motionX * speed;
        this.y += motionY * speed;
        
        // Don't let ball go out of bounds
        if(this.y + height - motionY > pong.height || this.y + motionY < 0)
        {
            // Bottom bound
            if(this.motionY < 0)
            {
                this.y = 0;
                this.motionY = random.nextInt(4);

                if(motionY == 0)  motionY = 1;
            }
            
            // Upper bound
            else
            {
                this.motionY = -random.nextInt(4);
                this.y = pong.height - height;

                if(motionY == 0) motionY = -1;
            }
        }
        
        // Ball hits paddle 1
        if(checkCollision(paddle1) == 1)
        {
            this.motionX = 1;// + (amountOfHits / 5);
            this.motionY = -2 + random.nextInt(4);

            if(motionY == 0) motionY = 1;

            //amountOfHits++;
        }
        
        // Ball hits paddle 2
        else if(checkCollision(paddle2) == 1)
        {
            this.motionX = -1;// - (amountOfHits / 5);
            this.motionY = -2 + random.nextInt(4);

            if(motionY == 0) motionY = 1;

            //amountOfHits++;
        }
        
        // Ball passes paddle 1
        if(checkCollision(paddle1) == 2)
        {
            paddle2.score++;
            spawn();
        }
        
        // Ball passes paddle 2
        else if(checkCollision(paddle2) == 2)
        {
            paddle1.score++;
            spawn();
        }
    }
    
    // Put new ball in center 
    public void spawn()
    {
        //this.amountOfHits = 0;
        this.x = pong.width / 2 - this.width / 2;
        this.y = pong.height / 2 - this.height / 2;

        this.motionY = -2 + random.nextInt(4);

        if(motionY == 0) motionY = 1;

        if(random.nextBoolean()) motionX = 1;
        else    motionX = -1;
    }
    
    // Check where the ball hits; 0 = nothing, 1 = bounce, 2 = score
    public int checkCollision(Paddle paddle)
    {
        if(this.x < paddle.x + paddle.width && this.x + width > paddle.x && this.y < paddle.y + paddle.height && this.y + height > paddle.y)
            return 1; 
        
        else if((paddle.x > x && paddle.paddleNumber == 1) || (paddle.x < x - width && paddle.paddleNumber == 2))
            return 2;

        return 0;
    }

    public void render(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
    }
}