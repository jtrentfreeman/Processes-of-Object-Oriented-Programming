package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Brick {
    
    public int x, y, width, height;
    public int points = 1;
    public Pong pong;
    public Item item;
    
    public Brick(Pong pong, int x, int y)
    {
        this.height = (pong.height - 100) / 5;  // h = 120
        this.width = pong.width/35;             // w = 40
        this.pong = pong;
        this.x = x ;
        this.y = y;
    }
    
    public void setItem(int effect, int number)
    {
        this.item = new Item( this.pong, effect, this, number );
    }
    
    public int getX() { return this.x; }
    
    public int getY() { return this.y; }
    
    public int getWidth() { return this.width; }
    
    public int getHeight() { return this.height; }
    
    public Item getItem() { return this.item; }
    
    // Update the brick's visibility
    // True means to delete brick
    public boolean update(Ball ball, Paddle p1, Paddle p2)
    {
        // Check if touches ball
        if(this.x <= ball.x + ball.width && this.x + width >= ball.x && this.y <= ball.y + ball.height && this.y + height >= ball.y)
        {
            Audio player = new Audio("brick");
            player.runSound();
            
            ball.motionX = -(ball.motionX);
            
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