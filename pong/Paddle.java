package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle
{
    public int paddleNumber;
    public int x, y, width = 35, height = 250, originalHeight = 250;
    private int powerUpDuration = 0;
    private int maxHealth = 3;
    private int health;
    private Pong pong;
    public int score;
    public int direction = 0;

    public Paddle(Pong pong, int paddleNumber)
    {
        if(paddleNumber == 1)
            originalHeight = 260 - 10*pong.gameDifficulty;
        if(pong.bot && paddleNumber == 2)
            originalHeight = 300;
        height = originalHeight;
        health = maxHealth;
        
        this.paddleNumber = paddleNumber;
        this.pong = pong;

	// Both x locations are on the left side of the paddle
        if(paddleNumber == 1) x = 0;
        if(paddleNumber == 2) x = pong.width - width;
	
	// y positioned at the center of the paddle
        y = pong.height / 2 - this.height / 2;
    }

    // Move the paddle at a constant rate while pressed
    // Pong.java utilizes keyPressed & keyReleased to keep velocity
    public void move(boolean up)
    {
        int speed = 15;

        if (up)
        {
            if(y - speed > 0) 
                y -= speed;
            else 
                y = 0;
        }
        else
        {
            if(y + speed + height < Pong.pong.height)
                y += speed;
            else
                y = Pong.pong.height - height;
        }
    }
    
    // 0: up paddle size, 1: add point, 2: damage, 3: health
    public void affectPaddle( int effect )
    {
        if( effect == 0 )
        {
            // Adjust paddle position so growth doesn't extend out of bounds
            if( powerUpDuration == 0 )
            {
                if( y + height + 50 >= pong.height )
                    y -= 50;
                else if( y - height / 2 > 25 )
                    y -= 25;
            }
            
            powerUpDuration = 200;
            height = originalHeight - 5*(pong.gameDifficulty);
            
        }
        else if( effect == 1 )
            this.score++;
        
        else if( effect == 2 )
            this.health--;
        
        else if( effect == 3 )
            if( health < maxHealth )
                this.health++;
        
    }
    
    public void instantWin( int threshhold )
    {
        this.score = threshhold;
    }
    
    public int getHealth()
    {
        return this.health;
    }
    
    public boolean hasPower()
    {
        return this.powerUpDuration > 0;
    }
    
    // Back to original height
    public void reducePowerDuration()
    {
        powerUpDuration--;
        if( powerUpDuration == 0 )
            height = 250;
        
    }
    
    public void render(Graphics g)
    {
        if( health <= 1 )
            g.setColor(Color.RED);
        else
            g.setColor(Color.WHITE);
        
        g.fillRect(x, y, width, height);
    }
}