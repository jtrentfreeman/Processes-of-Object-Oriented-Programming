package pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener
{
    public static Pong pong;
    public int width = 1400, height = 700;
    public Renderer renderer;
    public Paddle player1;
    public Paddle player2;
    public Ball ball;
    public Ball ball2;
    
    public Brick[] bricks;
    public Item[] items;
    public int numBricksLeft = 0;
    public boolean bot = false, selectingDifficulty, helpMenu;
    public boolean w, s, up, down;
    public int gameDifficulty = 5;

    // 0 = Menu, 1 = Paused, 2 = Playing, 3 = Over
    public int gameStatus = 0, scoreLimit = 15, playerWon;

    public int botDifficulty, botMoves, botCooldown = 0;
    public boolean Windows;
    public Random random;
    public JFrame jframe;
    public BufferedImage image;
    
    public static void main(String[] args)
    {
        MainMenu mm = new MainMenu();
        mm.setUpMenu();
        // pong = new Pong();
        
    	Audio player = new Audio("Automation");
        player.runBGM();
    }


	public Pong()
    {
        Timer timer = new Timer(20, this);
        random = new Random();
        
        jframe = new JFrame("Brick Pong");
        renderer = new Renderer();

        jframe.setSize(width + 16, height + 39);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderer);
        jframe.addKeyListener(this);


        // Save background image for update()
        try {
            String OS = System.getProperty("os.name");
            Windows = false;

            if( OS.length() > 8 )
                Windows = "Windows".equals(OS.substring(0,7));

            // Specify proper file path and have .wav audio file.
            if( Windows )
                image = ImageIO.read(new File("./src/pong/genericBackground.jpg"));
            else
                image = ImageIO.read(new File("./pong/genericBackground.jpg"));
            
        } catch(Exception e) {
        }
        
        timer.start();
    }

    public void start()
    {
        gameStatus = 2;
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);
        ball = new Ball(this);
        ball2 = new Ball(this);
        
        ball.setOtherBall(ball2);
        ball2.setOtherBall(ball);
        ball.side = 1;
        ball2.side = 2;
        
        ball.firstSpawn();
        ball2.firstSpawn();
        
        // 10 x 5 grid of bricks
        bricks = new Brick[50];
        items = new Item[50];
        for( int i = 0; i < 5; i++)
        {
            for( int j = 0; j < 10; j++)
            {   
                // Middle 3/7 of screen, gaps of 20
                Brick b = new Brick(pong, pong.width * 2/7 + (j*60) + 10, pong.height * i/5 +10);
                bricks[10*i+j] = b;
            }
        }
        
        // Generate Bricks
        Scanner scanner;
        try {

            // Get correct path
            if( Windows )
                scanner = new Scanner( new File("./src/pong/BrickGrid.txt"));
            else
                scanner = new Scanner( new File("./pong/BrickGrid.txt"));

            String map = "";
            String more;
            while( scanner.hasNextLine() )
            {
                more = scanner.nextLine();
                map = map.concat(more);
            }

            // Set blocks to null, give item, or neither
            // 
            for( int i = 0; i < 5; i++)
            {
                for( int j = 0; j < 10; j++)
                {
                    // 2: up paddle size, 3: add point, 4: damage, 5: health
                    if( map.charAt(10*i+j) == '0' )
                        bricks[10*i+j] = null;
                    
                    else if( map.charAt(10*i+j) == '2' )
                        bricks[10*i+j].setItem(0,10*i+j);
                    
                    else if( map.charAt(10*i+j) == '3' )
                        bricks[10*i+j].setItem(1,10*i+j);
                    
                    else if( map.charAt(10*i+j) == '4' )
                        bricks[10*i+j].setItem(2,10*i+j);
                    
                    else if( map.charAt(10*i+j) == '5' )
                        bricks[10*i+j].setItem(3,10*i+j);
                    
                    // Random non-null if unknown char
                    else if( map.charAt(10*i+j) != '1' )
                        bricks[10*i+j].setItem((int) ( Math.random() * 4) + 1, 10*i+j);
                    
                    numBricksLeft++;
                }
            }

            scanner.close();
            
        // If FNF, all bricks will be shown without crashing
        } catch (FileNotFoundException ex) {
            
        }
        
    }
    
    public void deleteItem( int number )
    {
        this.items[number] = null;
    }

    public void update()
    {
        
        // Bricks & Items
        for( int i = 0; i < bricks.length; i++)
        {
            Brick b = bricks[i];
            
            // Check existence
            if( b != null && ( b.update(ball, player1, player2) || b.update(ball2, player1, player2)) )
            {
                Item brickItem = b.getItem();
                
                // If has item, spawn it
                if( brickItem != null )
                {
                    items[i] = brickItem;
                    brickItem.spawn();
                }
                
                // Delete Brick
                bricks[i] = null;
                numBricksLeft--;
            }
        }
        
        if (player1.score >= scoreLimit)
        {
            playerWon = 1;
            gameStatus = 3;
        }

        if (player2.score >= scoreLimit)
        {
            gameStatus = 3;
            playerWon = 2;
        }

        if (w)
            player1.move(true);

        if (s)
            player1.move(false);

        if (!bot)
        {
            if (up)
                player2.move(true);

            if (down)
                player2.move(false);
        }
        else
        {
            if (botCooldown > 0)
            {
                botCooldown--;

                if (botCooldown == 0)
                    botMoves = 0;
            }

            if (botMoves < 10)
            {
                Ball closestBall = ball.x >= ball2.x ? ball : ball2;
                
                if (player2.y + player2.height / 2 < closestBall.y)
                {
                    player2.direction = -2;
                    player2.move(false);
                    botMoves++;
                }

                if (player2.y + player2.height / 2 > closestBall.y)
                {
                    player2.direction = 2;
                    player2.move(true);
                    botMoves++;
                }
		
		// Delay on bot movement
                if (botDifficulty == 0)
                    botCooldown = 20;

                if (botDifficulty == 1)
                    botCooldown = 15;

                if (botDifficulty == 2)
                    botCooldown = 10;
            }
        }

        ball.update(player1, player2);
        ball2.update(player1, player2);
        
        for( Item item : this.items )
            if( item != null )
                item.update(player1, player2);
        
        if( player1.hasPower() )
            player1.reducePowerDuration();
        
        if( player2.hasPower() )
            player2.reducePowerDuration();
        
        // Tie, pick random winner :P
        if( player1.getHealth() == 0 && player2.getHealth() == 0)
            if( (int) (Math.random() * 2) == 1)
                player2.instantWin( scoreLimit );
        
        // Player 1 died, player 2 wins
        if( player1.getHealth() == 0 )
            player2.instantWin( scoreLimit );
        
        // Player 2 died, player 1 wins
        if( player2.getHealth() == 0 )
            player1.instantWin( scoreLimit );
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (gameStatus == 2)
            update();

        renderer.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int id = e.getKeyCode();
		
        if (id == KeyEvent.VK_W)
            w = true;

        else if (id == KeyEvent.VK_S)
            s = true;

        else if (id == KeyEvent.VK_UP)
            up = true;

        else if (id == KeyEvent.VK_DOWN)
            down = true;

        else if (id == KeyEvent.VK_RIGHT || id == KeyEvent.VK_D)
        {
            if (selectingDifficulty)
            {
                if (botDifficulty < 2)
                    botDifficulty++;

                else
                    botDifficulty = 0;

            }
            else if (gameStatus == 0)
                scoreLimit++;
        }
        else if (id == KeyEvent.VK_LEFT || id == KeyEvent.VK_A)
        {
            if (selectingDifficulty)
            {
                if (botDifficulty > 0)
                    botDifficulty--;

                else
                    botDifficulty = 2;
            }
            else if (gameStatus == 0 && scoreLimit > 1)
                scoreLimit--;
        }

        // Enter main menu
        else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3))
            gameStatus = 0;
	    
        // View help menu
        else if (id == KeyEvent.VK_H && gameStatus == 0 && helpMenu == false)
        {
            helpMenu = true;
        }

        // Close help menu
        else if (id == KeyEvent.VK_H && gameStatus == 0 && helpMenu == true)
        {
            helpMenu = false;
        }

        // Enter bot menu
        else if (id == KeyEvent.VK_SHIFT && gameStatus == 0 && helpMenu == false)
        {
            bot = true;
            selectingDifficulty = true;
        }
        
        // Start game or pause
        else if (id == KeyEvent.VK_SPACE && helpMenu == false)
        {
            if (gameStatus == 0 || gameStatus == 3)
            {
                // Don't start game with bot
                if (!selectingDifficulty)
                    bot = false;
                
                // Prevents starting over at bot menu
                else
                    selectingDifficulty = false;

                start();
            }
            
            // Unpause
            else if (gameStatus == 1)
                gameStatus = 2;

            // Pause
            else if (gameStatus == 2)
                gameStatus = 1;
        }
        
        // Momentum for slight ball motion
        if(gameStatus == 1 || gameStatus == 2)
        {
            if(w)
                player1.direction = -2;
            if(s)
                player1.direction = 2;
            if( !up )
                player2.direction = -2;
            if( !down )
                player2.direction = 2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W)
            w = false;

        else if (id == KeyEvent.VK_S)
            s = false;

        else if (id == KeyEvent.VK_UP)
            up = false;

        else if (id == KeyEvent.VK_DOWN)
            down = false;
        
        // Momentum for slight ball motion
        if(gameStatus == 2)
        {
            if( !w && !s )
                player1.direction = 0;
            if( !bot && !up && !down )
                player2.direction = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // Empty
    }
    
    // Renders the menu and board
    public void render(Graphics2D g) throws IOException
    {
        // Background
        g.drawImage( image, 0, 0, null);
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        // Enter menu
        if (gameStatus == 0 && helpMenu == false)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));

            g.drawString("Brick Pong", width / 2 - 140, 50);

            // Main menu
            if (!selectingDifficulty)
            {
                g.setFont(new Font("Arial", 1, 30));

                g.drawString("Press Space to Play", width / 2 - 150, height / 2 - 25);
                g.drawString("Press Shift to Play with Bot", width / 2 - 200, height / 2 + 25);
                g.drawString("<< Score Limit: " + scoreLimit + " >>", width / 2 - 150, height / 2 + 75);
            }
            
            numBricksLeft = 0;
        }

        // Bot menu
        if (selectingDifficulty)
        {
            String string = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : "Hard");

            g.setFont(new Font("Arial", 1, 30));

            g.drawString("<< Bot Difficulty: " + string + " >>", width / 2 - 180, height / 2 - 25);
            g.drawString("Press Space to Play", width / 2 - 150, height / 2 + 25);
            
            numBricksLeft = 0;
        }
	
        // help menu
        if (helpMenu)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("HELP MENU", width / 2 - 180, height / 2 - 250);
            g.setFont(new Font("Arial", 1, 20));
            String nav = "Navigation";
            String paddle = "Paddle";
            String close = "Close Help";
            String navSpace = "space - multiplayer";
            String navShift = "shift - singleplayer";
            String navChoose = "a/d or left/right arrows to choose settings";
            String player1String = "Player 1 - w (up), s (down)";
            String player2String = "Player 2 - up arrow (up), down arrow (down)";
            g.drawString(nav, 100, height / 2 - 180);
            g.drawString(navSpace, 150, height / 2 - 140);
            g.drawString(navShift, 150, height / 2 - 100);
            g.drawString(navChoose, 150, height / 2 - 60);
            g.drawString(paddle, 100, height / 2 - 20);
            g.drawString(player1String, 150, height / 2 + 20);
            g.drawString(player2String, 150, height / 2 + 60);
            g.drawString(close, 100, height / 2 + 100);
            g.drawString("h", 150, height / 2 + 140);
        }

        // Pause screen
        if (gameStatus == 1)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("Pause", width / 2 - 103, height / 2 - 25);
        }

        // Paused or Playing
        if (gameStatus == 1 || gameStatus == 2)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));

            g.drawString(String.valueOf(player1.score), width / 2 - 90, 50);
            g.drawString(String.valueOf(player2.score), width / 2 + 65, 50);

            player1.render(g);
            player2.render(g);
            ball.render(g);
            ball2.render(g);
            
            // Bricks
            for (Brick b : bricks) {
                if(b != null)
                    b.render(g);
            }
            
            // Items
            for( Item item : this.items )
                if( item != null )
                    item.render(g);
            
        }

        // Game Over
        if (gameStatus == 3)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));

            g.drawString("PONG", width / 2 - 75, 50);

            if (bot && playerWon == 2)
                g.drawString("The Bot Wins!", width / 2 - 170, 200);

            else
                g.drawString("Player " + playerWon + " Wins!", width / 2 - 165, 200);

            g.setFont(new Font("Arial", 1, 30));

            g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
            g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);
            
            numBricksLeft = 0;
        }
    }

}
