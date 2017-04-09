package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Brick {
    
    public int x, y, width = 30, height = 50;
    public int points = 1;
    //public int motionX, motionY; // possible feature
    
    public Brick(Pong pong, int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    // Update the brick's visibility
    // True means to delete brick
    public boolean update(Ball ball, Paddle p1, Paddle p2)
    {
        // Check if touches ball
        if(this.x < ball.x + ball.width && this.x + width > ball.x && this.y < ball.y + ball.height && this.y + height > ball.y)
        {
            Audio player = new Audio("brick");
            player.runSound();
            
            ball.motionX = -1;
            
            if(ball.lastHit == 1)
                p1.score += this.points;
            
            else if(ball.lastHit == 2)
                p2.score += this.points;
            
            return true;
        }
        return false;
    }
    
    public void render(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
}
