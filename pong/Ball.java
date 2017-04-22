package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{
    public int x, y, width = 25, height = 25;
    public int motionX, motionY;
    public int baseSpeed = 15;
    public int side = 1;
    public Random random;
    public Pong pong;
    public Ball twin;
    public int lastHit = 0;
    
    public Ball(Pong pong)
    {
        this.pong = pong;
        this.baseSpeed += pong.gameDifficulty;
        this.random = new Random();
    }
    
    public void setOtherBall( Ball ball )
    {
        twin = ball;
    }
    
    // Update the ball's movement
    public void update(Paddle paddle1, Paddle paddle2)
    {
        int speed;
        
        if( pong.numBricksLeft <= 8)
            speed = 23;
        else if( pong.numBricksLeft > 14)
            speed = baseSpeed - 2;
        else
            speed = baseSpeed;
        
        // No brick stealing
        if( lastHit == 1 && x >= pong.width/2 )
            lastHit = 0;
        else if( lastHit == 2 && x <= pong.width/2)
            lastHit = 0;

        this.x += motionX * speed;
        this.y += motionY * speed;
        
        // Left or Right side
        if( x <= pong.width / 2)
            side = 1;
        else
            side = 2;
        
        // Don't let ball go out of bounds
        if(this.y + height - motionY > pong.height || this.y + motionY < 0)
        {
            sound("wall");
            
            // Bottom bound
            if(this.motionY < 0)
            {
                this.y = 0;
                this.motionY = random.nextInt(2);

                if(motionY == 0)  motionY = 1;
            }
            
            // Upper bound
            else
            {
                this.motionY = -random.nextInt(2);
                this.y = pong.height - height;

                if(motionY == 0) motionY = -1;
            }
        }
        
        // Ball hits paddle 1
        if(checkCollision(paddle1) == 1)
        {
            sound("paddle");
            lastHit = 1;
            this.motionX = 1;
            this.motionY = this.motionY + random.nextInt(1) * paddle1.direction;

            if(motionY == 0) motionY = 1;

        }
        
        // Ball hits paddle 2
        else if(checkCollision(paddle2) == 1)
        {
            sound("paddle");
            lastHit = 2;
            this.motionX = -1;
            this.motionY = this.motionY + random.nextInt(1) * paddle2.direction;

            if(motionY == 0) motionY = 1;

        }
        
        // Ball passes paddle 1
        if(checkCollision(paddle1) == 2)
        {
            sound("score");
            paddle2.score += 3;
            spawn();
        }
        
        // Ball passes paddle 2
        else if(checkCollision(paddle2) == 2)
        {
            sound("score");
            paddle1.score += 3;
            spawn();
        }
    }
    
    private void sound(String fileName) {
        Audio player = new Audio(fileName);
        player.runSound();
    }
    
    // Called by pong
    public void firstSpawn()
    {
        this.spawn();
    }
    
    // Put new ball in center 
    private void spawn()
    {
        lastHit = 0;
        
        if( twin.side == 1)
        {
            this.x = pong.width * 8/10 - this.width / 2;
            this.motionX = -1;
        }
        else
        {
            this.x = pong.width * 2/10 - this.width / 2;
            this.motionX = 1;
        }
            
        this.y = pong.height / 2 - this.height / 2;

        this.motionY = -1 + random.nextInt(2);

        if(motionY == 0) motionY = 1;
        
    }
    
    // Check where the ball hits; 0 = nothing, 1 = bounce, 2 = score
    private int checkCollision(Paddle paddle)
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