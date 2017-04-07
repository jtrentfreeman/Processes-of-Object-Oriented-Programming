package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle
{
    public int paddleNumber;
    public int x, y, width = 35, height = 250;
    public int score;

    public Paddle(Pong pong, int paddleNumber)
    {
        this.paddleNumber = paddleNumber;

	// Both x locations are on the left side of the paddle
        if(paddleNumber == 1) this.x = 0;
        if(paddleNumber == 2) this.x = pong.width - width;
	
	// y positioned at the center of the paddle
        this.y = pong.height / 2 - this.height / 2;
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
    
    public void render(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
}