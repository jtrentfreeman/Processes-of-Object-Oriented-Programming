
package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener
{
    public static Pong pong;
    public int width = 1350, height = 700;
    private Renderer renderer;
    private Paddle player1;
    private Paddle player2;
    private Ball ball;
    private Ball ball2;
    
    public int numBricksLeft = 0;
    private Brick[] bricks;
    private Item[] items;
    private boolean w, s, up, down;
    private int botDifficulty, botMoves, botCooldown = 0, endGameTimer;
    private boolean helpMenu, leaderBoard, timer = true;
    public boolean bot = false, selectingDifficulty;
    
    // 0 custom, 1-2 easy, 3-4 moderate, 5-6 hard
    public int gameDifficulty = 3;

    // 0 = Menu, 1 = Paused, 2 = Playing, 3 = Over
    private int gameStatus = 0, scoreLimit = 20, playerWon;

    private boolean Windows = false;
    private JFrame jframe;
    private BufferedImage image;
    
    public static void main(String[] args)
    {
        pong = new Pong();
	
	Audio player = new Audio("Automation");
        player.runBGM();
    }

    public Pong()
    {
        Timer timer = new Timer(20, this);
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
                image = ImageIO.read(new File("genericBackground.jpg")); // "genericBackground.jpg"
            else
                image = ImageIO.read(new File("genericBackground.jpg"));
            
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
        endGameTimer = 500;
        
        // 10 x 5 grid of bricks
        bricks = new Brick[50];
        items = new Item[50];
        for( int i = 0; i < 5; i++)
        {
            for( int j = 0; j < 10; j++)
            {   
                // Middle 3/7 of screen
                Brick b = new Brick(pong, pong.width * 2/7 + (j*60) + 10, pong.height * i/5 +10);
                bricks[10*i+j] = b;
            }
        }
        
        // Generate Bricks
        Scanner scanner;
        try {

            // Get correct path
            if( Windows )
                scanner = new Scanner( new File("BrickGrid.txt")); // "./src/pong/BrickGrid.txt"
            else
                scanner = new Scanner( new File("BrickGrid.txt")); // "./src/pong/BrickGrid.txt"

            String map = "";
            String more;
            while( scanner.hasNextLine() )
            {
                more = scanner.nextLine();
                map = map.concat(more);
            }

            // Set blocks to null, give item, or neither
            int current;
            for( int i = 0; i < 5; i++)
            {
                for( int j = 0; j < 10; j++)
                {
                    // Displacement for brick design from BrickGrid.txt
                    current = (gameDifficulty * 50) + 10*i+j;
                    
                    // 2: up paddle size, 3: add point, 4: damage, 5: health
                    if( map.charAt(current) == '0' )
                    {
                        bricks[10*i+j] = null;
                        continue;
                    }
                    
                    else if( map.charAt(current) == '2' )
                        bricks[10*i+j].setItem(2,10*i+j);
                    
                    else if( map.charAt(current) == '3' )
                        bricks[10*i+j].setItem(3,10*i+j);
                    
                    else if( map.charAt(current) == '4' )
                        bricks[10*i+j].setItem(4,10*i+j);
                    
                    else if( map.charAt(current) == '5' )
                        bricks[10*i+j].setItem(5,10*i+j);
                    
                    // Random non-null if unknown char
                    else if( map.charAt(current) != '1' )
                        bricks[10*i+j].setItem((int) ( Math.random() * 4) + 1, 10*i+j);
                    
                    numBricksLeft++;
                }
            }

            scanner.close();
            
        // If FNF, all bricks will be shown without crashing
        } catch (FileNotFoundException ex) {
        }
        
    }
    
    // Called when item is caught or missed
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
        
        if( numBricksLeft <= 3 && timer == true )
        {
            endGameTimer--;
        }
        
        if( endGameTimer <= 0 )
        {
            if( player1.score > player2.score )
            {
                playerWon = 1;
                gameStatus = 3;
            } else {
                gameStatus = 3;
                playerWon = 2;
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
        } else {
            if (botCooldown > 0)
            {
                botCooldown--;

                if (botCooldown == 0)
                    botMoves = 0;
            }

            if (botMoves < 10)
            {
                // AI picks target
                // Both going right -> target closest
                Ball target = ball2;
                if( ball.motionX > 0 && ball2.motionX > 0)
                    target = ball.x >= ball2.x ? ball : ball2;
                else if( ball.side == 2 && ball2.side == 1 )
                    target = ball;
                else if( ball.side == 1 && ball2.side == 2 )
                    target = ball2;
                
                if (player2.y + player2.height / 2 < target.y)
                {
                    player2.direction = -2;
                    player2.move(false);
                    botMoves++;
                }

                if (player2.y + player2.height / 2 > target.y)
                {
                    player2.direction = 2;
                    player2.move(true);
                    botMoves++;
                }
		
		// Delay on bot movement
                if (botDifficulty == 0)
                    botCooldown = 10;

                if (botDifficulty == 1)
                    botCooldown = 4;

                if (botDifficulty == 2)
                    botCooldown = 2;
            }
        }

        ball.update(player1, player2);
        ball2.update(player1, player2);
        
        // Update item
        for( Item item : this.items )
            if( item != null )
                item.update(player1, player2);
        
        // Timer on player power-up
        if( player1.hasPower() )
            player1.reducePowerDuration();
        if( player2.hasPower() )
            player2.reducePowerDuration();
        
        // Player 1 died, player 2 wins (Bot will win in the event of a tie)
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

    //keyPressed and keyReleased for smooth paddle movement
    @Override
    public void keyPressed(KeyEvent e)
    {
        int id = e.getKeyCode();
        
        // Toggle if a timer is set after most of the bricks are gone
        if (id == KeyEvent.VK_T)
        {
            if(timer) timer = false;
            if(timer) timer = true;
        }
        
        else if (id == KeyEvent.VK_W)
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

        // Go back to main menu
        else if (id == KeyEvent.VK_ESCAPE)
        {
            bot = false;
            selectingDifficulty = false;
            gameStatus = 0;
            leaderBoard = false;
            helpMenu = false;
        }

        // Change game difficulty from main menu
        else if (id == KeyEvent.VK_0 && gameStatus == 0 )
        {
            botDifficulty = 1;
            scoreLimit = 30;
            gameDifficulty = 0;
        }
        else if (id == KeyEvent.VK_1 && gameStatus == 0 )
        {
            botDifficulty = 0;
            scoreLimit = 15;
            gameDifficulty = 1;
        }
        else if (id == KeyEvent.VK_2 && gameStatus == 0 )
        {
            botDifficulty = 0;
            scoreLimit = 22;
            gameDifficulty = 2;
        }
        else if (id == KeyEvent.VK_3 && gameStatus == 0 )
        {
            botDifficulty = 1;
            scoreLimit = 25;
            gameDifficulty = 3;
        }
        else if (id == KeyEvent.VK_4 && gameStatus == 0 )
        {
            botDifficulty = 1;
            scoreLimit = 25;
            gameDifficulty = 4;
        }
        else if (id == KeyEvent.VK_5 && gameStatus == 0 )
        {
            botDifficulty = 2;
            scoreLimit = 25;
            gameDifficulty = 5;
        }
        else if (id == KeyEvent.VK_6 && gameStatus == 0 )
        {
            botDifficulty = 2;
            scoreLimit = 30;
            gameDifficulty = 6;
        }

        // Show leaderboard
        else if (id == KeyEvent.VK_L && gameStatus == 0 && leaderBoard == false)
        {
            leaderBoard = true;
            helpMenu = false;
        }

        // Hide leaderboard
        else if (id == KeyEvent.VK_L && gameStatus == 0 && leaderBoard == true)
            leaderBoard = false;
        
	// Show help menu
        else if (id == KeyEvent.VK_H && gameStatus == 0 && helpMenu == false)
        {
            helpMenu = true;
            leaderBoard = false;
        }

        // Hide help menu
        else if (id == KeyEvent.VK_H && gameStatus == 0 && helpMenu == true)
            helpMenu = false;

        // Enter bot menu (bot difficulty selection)
        else if (id == KeyEvent.VK_SHIFT && gameStatus == 0 && helpMenu == false)
        {
            bot = true;
            selectingDifficulty = true;
        }
        
        // Start game or pause
        else if (id == KeyEvent.VK_SPACE && helpMenu == false)
        {
            // Don't start game with bot
            if (gameStatus == 0)
            {
                if (!selectingDifficulty)
                    bot = false;
                
                // Prevents starting over at bot menu
                else
                    selectingDifficulty = false;
                
                start();
            } else if (gameStatus == 3)
            {
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
        if (gameStatus == 0 && helpMenu == false && leaderBoard == false)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));

            g.drawString("Brick Pong", width / 2 - 140, 50);

            // Main menu
            if (!selectingDifficulty)
            {
                g.setFont(new Font("Arial", 1, 30));

                g.drawString("Select a Difficulty: 0, 1, 2, 3, 4, 5, 6", width / 2 - 235, height / 2 - 25);
                g.drawString("Press Shift to Play with Bot", width / 2 - 180, height / 2 + 25);
                g.drawString("< Score Limit: " + scoreLimit + " >", width / 2 - 125, height / 2 + 75);
                
                g.setFont(new Font("Arial", 1, 20));
                g.drawString("Open Help", 100, height / 2 + 100);
                g.drawString("H", 150, height / 2 + 140);
            }
            
            numBricksLeft = 0;
        }

        // Bot menu
        if (selectingDifficulty)
        {
            String string = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : "Hard");

            g.setFont(new Font("Arial", 1, 30));

            g.drawString("Bot Difficulty: " + string , width / 2 - 150, height / 2 - 25);
            g.drawString("Press Space to Play", width / 2 - 150, height / 2 + 25);
        }
       
        if(leaderBoard)
        {
            Scanner scan = new Scanner( new File("LeaderBoard.txt"));
            String noRecord = "No record";
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("LEADERBOARD", width / 2 - 180, height / 2 - 250);
            g.setFont(new Font("Arial", 1, 20));
            
            for( int i = 1; i < 6; i++)
            {
                if( scan.hasNext() )
                    g.drawString("#" + i + " - " + scan.next(), 100, height / 2 - (180 - 40*i));
                else
                    g.drawString(noRecord, 100, height / 2 - (180 - 40*i));
            }
        }
        
	// help menu
	if (helpMenu)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("HELP MENU", width / 2 - 150, height / 2 - 250);
            g.setFont(new Font("Arial", 1, 20));
            
            g.drawString("Menu Navigation", 100, height / 2 - 180);
            g.drawString("space - multiplayer", 150, height / 2 - 140);
            g.drawString("shift - singleplayer", 150, height / 2 - 100);
            g.drawString("a/d or left/right arrows to choose settings", 150, height / 2 - 60);
            g.drawString("Paddle", 100, height / 2 - 20);
            g.drawString("Player 1 - w (up), s (down)", 150, height / 2 + 20);
            g.drawString("Player 2 - up arrow (up), down arrow (down)", 150, height / 2 + 60);
            g.drawString("Close Help", 100, height / 2 + 100);
            g.drawString("H", 150, height / 2 + 140);
            g.drawString("Open Leaderboard", 100, height / 2 + 180);
            g.drawString("L", 150, height / 2 + 220);
            g.drawString("Return to main menu", 100, height / 2 + 260);
            g.drawString("ESC", 150, height / 2 + 300);
            
        }

        // Paused or Playing
        if (gameStatus == 1 || gameStatus == 2)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));

            g.drawString(String.valueOf(player1.score), width * 3/10 - 90, 50);
            g.drawString(String.valueOf(player2.score), width * 7/10 + 65, 50);

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

        // Pause screen
        if (gameStatus == 1)
        {
            g.setColor(Color.DARK_GRAY);
            g.fillRect( width/2 - 210, height/2 - 110, 420, 145);
            //g.fillRect( 0, height/2 - 110, width, 145);
            
            g.setColor(Color.BLACK);
            g.fillRect( width/2 - 200, height/2 - 100, 400, 125);
            //g.fillRect( 0, height/2 - 100, width, 125);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("Pause", width / 2 - 75, height / 2 - 19);
        }

        // Game Over
        if (gameStatus == 3)
        {
            g.setColor(Color.WHITE);
            
            if( endGameTimer == 0)
            {
                g.setFont(new Font("Arial", 1, 35));
                g.drawString("Time ran out!", width / 2 - 110, 150);
            }
            
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
