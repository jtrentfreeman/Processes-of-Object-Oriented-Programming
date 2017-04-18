package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Item 
{
    private int effect;
    private int x, y, width = 20, height = 20;
    private int speed = 5;
    private int number;
    private Pong pong;
    private Brick brick;

    // To do:
    // adjust A.I. to recognize
    // 

    public Item(Pong pong, int effect, Brick brick, int number)
    {
        //this.x = brick.getX() + (brick.getWidth() / 2);
        //this.y = brick.getY() + (brick.getHeight() / 2);
        this.number = number;
        this.effect = effect;
        this.brick = brick;
        this.pong = pong;
        
        /*// Prepare random direction (L or R)
        if((int) (Math.random() * 2) == 1)
            this.speed = - this.speed;
        */
        
        // Prepare direction by board side
        if( number % 10 < 5 )
            this.speed = - this.speed;
        
    }

    public void update(Paddle paddle1, Paddle paddle2)
    {
        this.x += speed;

        // Item hits paddle 1
        if(checkCollision(paddle1) == 1)
        {
            //sound("item" ++ effect);
            paddle1.affectPaddle( this.effect );
            this.pong.deleteItem(this.number);
        }

        // Item hits paddle 2
        if(checkCollision(paddle2) == 1)
        {
            //sound("item" ++ effect);
            paddle2.affectPaddle( this.effect );
            this.pong.deleteItem(this.number);
        }

        // Item passes paddle 1
        if(checkCollision(paddle1) == 2)
        {
            this.pong.deleteItem(this.number);
        }

        // Item passes paddle 2
        if(checkCollision(paddle2) == 2)
        {
            this.pong.deleteItem(this.number);
        }
    }

    // Currently, no file exists
    /*private void sound(String fileName)
    {
        Audio player = new Audio(fileName);
        player.runSound();
    }*/

    // Put item where brick was
    // Spawns after brick breaks
    public void spawn()
    {
        this.x = brick.getX() + (brick.getWidth() / 2);
        this.y = brick.getY() + (brick.getHeight() / 2);
    }

    // Check if caught; 0 = no, 1 = yes, 2 = miss
    private int checkCollision(Paddle paddle)
    {
        if( this.x < paddle.x + paddle.width && this.x + this.width > paddle.x && this.y < paddle.y + paddle.height && this.y + this.height > paddle.y)
            return 1;

        else if((paddle.x > x && paddle.paddleNumber == 1) || (paddle.x < x - width && paddle.paddleNumber == 2))
            return 2;

        return 0;
    }
    
    public void render( Graphics g )
    {
        // 0: up paddle size, 1: add point, 2: damage, 3: health
        if( effect == 0 )
            g.setColor(Color.YELLOW);
        
        else if( effect == 1 )
            g.setColor(Color.GREEN);
        
        else if( effect == 2 )
            g.setColor(Color.RED);
        
        else if( effect == 3 )
            g.setColor(Color.PINK);
        
        else
            g.setColor(Color.WHITE);
        
        g.fillOval(x, y, width, height);
    }
}