package pong;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainMenu {

	int levelsComplete = 0;
    public int width = 1400, height = 700;
	
	// frames
	private JFrame menu;
	private JFrame levelMenu;
	private JFrame options;
	private JFrame help;
	
	
	// buttons for main menu
	private JButton p1;
	private JButton p2;
	private JButton op;
	private JButton hp;
	private JButton qt;
	private JButton titleText;	// could/should probably be a JLabel but it works right now
	
	// buttons for options menu
	private JButton backOptions;	// go back to main menu from options
	private JButton backQuit;
	private JButton backHelp;		// go back to main menu from help
	private JButton mOn;			// music icon 3 bars
	private JButton mLow;			// music icon 1 bar
	private JButton mOff;			// music icon 0 bars
	private JButton sOn;			// sound icon 3 bars
	private JButton sLow;			// sound icon 1 bar
	private JButton sOff;			// sound icon 0 bars
	
	// not quite sure how sound level is measured
	private double musicLevel;
	private double soundLevel;
	
	// holds width of menu frames
	private final static int WIDTH = 450;
	
	MainMenu()
	{
		
	}
	
	public void setUpMenu()
{
		
		// set up the initial frames
		menu = new JFrame("Main Menu");
		menu.setSize(1400, 700);
		
		levelMenu = new JFrame("Level Select");
		levelMenu.setSize(1400, 700);
		
		options = new JFrame("Options");
		options.setSize(1400, 700);
		
		help = new JFrame("Help");
		help.setSize(1400, 700);
		
		// grabs the images from NewMenu/src/resources (and also held in NewMenu/bin/resources) (For Eclipse)
		ImageIcon mOnIcon = new ImageIcon("musicHigh.png");
		ImageIcon mLowIcon = new ImageIcon("musicLow.png");
		ImageIcon mOffIcon = new ImageIcon("musicOff.png");
		ImageIcon sOnIcon = new ImageIcon("soundHigh.png");
		ImageIcon sLowIcon = new ImageIcon("soundLow.png");
		ImageIcon sOffIcon = new ImageIcon("soundOff.png");
		
//			ImageIcon mOnIcon = new ImageIcon(getClass().getResource("/resources/musicHigh.png"));
//			ImageIcon mLowIcon = new ImageIcon(getClass().getResource("/resources/musicLow.png"));
//			ImageIcon mOffIcon = new ImageIcon(getClass().getResource("/resources/musicOff.png"));
			
		// set the icons to their buttons
		mOn = new JButton(mOnIcon);
		mOn.setVisible(true);
		mLow = new JButton(mLowIcon);
		mLow.setVisible(true);
		mOff = new JButton(mOffIcon);
		mOff.setVisible(true);
		
		sOn = new JButton(sOnIcon);
		sOn.setVisible(true);
		sLow = new JButton(sLowIcon);
		sLow.setVisible(true);
		sOff = new JButton(sOffIcon);
		sOff.setVisible(true);
		
		JPanel mainLevelPanel = new JPanel();
		JPanel levelPanel = new JPanel(new GridLayout(4, 2, 5, 20));
		levelMenu.getContentPane().add(BorderLayout.CENTER, levelPanel);
		JButton level1 = new JButton("LEVEL 1");
		level1.setOpaque(true);
		JButton level2 = new JButton("LEVEL 2");
		level2.setOpaque(false);
		JButton level3 = new JButton("LEVEL 3");
		JButton level4 = new JButton("LEVEL 4");
		JButton level5 = new JButton("LEVEL 5");
		JButton level6 = new JButton("LEVEL 6");
		JButton backLevels = new JButton("BACK");
		JButton quitLevels = new JButton("QUIT");
		levelPanel.add(level1).setLocation(0, 0);
		levelPanel.add(level2).setLocation(0, 1);
		levelPanel.add(level3).setLocation(1, 0);
		levelPanel.add(level4).setLocation(1, 1);
		levelPanel.add(level5).setLocation(2, 0);
		levelPanel.add(level6).setLocation(2, 1);
		levelPanel.add(backLevels).setLocation(3, 0);
		levelPanel.add(quitLevels).setLocation(3, 1);
		levelPanel.setPreferredSize(new Dimension(175, 350));
		levelPanel.setBorder(BorderFactory.createEmptyBorder(50, 350, 50, 350));
		levelPanel.setVisible(true);

		
		// setting up the music option buttons
		JLabel mOnL = new JLabel(mOnIcon);
		mOnL.setIcon(mOnIcon);
		mOnL.setOpaque(true);
		mOnL.setBorder(new LineBorder(Color.BLACK));
		mOnL.setBackground(Color.WHITE);
		mOnL.setVisible(true);
		JLabel mLowL = new JLabel(mLowIcon);
		mLowL.setIcon(mLowIcon);
		mLowL.setOpaque(true);
		mLowL.setBorder(new LineBorder(Color.BLACK));
		mLowL.setBackground(Color.WHITE);
		JLabel mOffL = new JLabel(mOffIcon);
		mOffL.setIcon(mOffIcon);
		mOffL.setOpaque(true);
		mOffL.setBorder(new LineBorder(Color.BLACK));
		mOffL.setVisible(true);
		mOffL.setBackground(Color.WHITE);
		
		JButton space = new JButton("empty button");
		
		JLabel soundB = new JLabel("<html><center>Sound</center><br /><center>Volume</center></html>");
		JLabel musicB = new JLabel("<html><center>Music</center><br /><center>Volume</center></html>");
		
		// soundPanel will hold the music and sound buttons (off, low, high)
		JPanel soundPanel = new JPanel(new GridLayout(3, 4, -2, 20));
		options.getContentPane().add(BorderLayout.CENTER, soundPanel);
		
		soundPanel.add(soundB).setLocation(0, 0);
		soundPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		soundPanel.add(mOff).setLocation(0, 1);
		soundPanel.add(mLow).setLocation(0, 2);
		soundPanel.add(mOn).setLocation(0, 3);
		
		backOptions = new JButton("Back");
		backQuit = new JButton("Quit");
		
		soundPanel.add(musicB).setLocation(1, 0);
		soundPanel.add(sOff).setLocation(1, 1);
		soundPanel.add(sLow).setLocation(1, 2);
		soundPanel.add(sOn).setLocation(1, 3);
		
		soundPanel.add(space).setLocation(2, 1);
		soundPanel.add(space).setLocation(2, 0);
		soundPanel.add(backOptions).setLocation(2, 2);
		soundPanel.add(backQuit).setLocation(2,3);
		space.setVisible(false);
		backOptions.setVisible(true);

		// adding proper bits into the frame
		// still doesn't show icons, not sure why
		soundPanel.setVisible(true);
		soundPanel.setBorder(BorderFactory.createEmptyBorder(200, 100, 200, 100));	
		
		options.add(soundPanel);
		
		// setting up the main menu
		JPanel top = new JPanel();
		JLabel title = new JLabel("Breakout Ball");
		title.setFont(new Font("Verdana",1, 20));
		top.add(title);
		top.setBorder(new LineBorder(Color.BLACK));
		 menu.add(top);
		top.setVisible(true);
		//menu.add(top);
		
		// title of game
		titleText = new JButton("Breakout Ball");
		titleText.setBorder(new LineBorder(Color.black));
		titleText.setOpaque(false);
		titleText.setContentAreaFilled(false);
		titleText.setBorderPainted(false);
		titleText.setFont(new Font("Verdana", Font.PLAIN, 25));
		
		// buttons for menu navigation
		p1 = new JButton("1 Player");
		p1.setBorder(new LineBorder(Color.BLACK));
		p1.setBackground(Color.WHITE);
		p1.setOpaque(true);
		p2 = new JButton("2 Player");
		p2.setBorder(new LineBorder(Color.BLACK));
		p2.setBackground(Color.WHITE);
		p2.setOpaque(true);
		op = new JButton("Options");
		op.setBorder(new LineBorder(Color.BLACK));
		op.setBackground(Color.WHITE);
		op.setOpaque(true);
		hp = new JButton("Help");
		hp.setBorder(new LineBorder(Color.BLACK));
		hp.setBackground(Color.WHITE);
		hp.setOpaque(true);
		qt = new JButton("Quit");
		qt.setBorder(new LineBorder(Color.BLACK));
		qt.setBackground(Color.WHITE);
		qt.setOpaque(true);	
		// adding proper bits into main panel
		JPanel innerPanel = new JPanel(new GridLayout(0, 1, 50, 0));
		innerPanel.add(titleText);
		innerPanel.add(Box.createRigidArea(new Dimension(5, 10)));
		innerPanel.add(p1);
		innerPanel.add(p2);
		innerPanel.add(op);
		innerPanel.add(hp);
		innerPanel.add(qt);
		innerPanel.setOpaque(false);
		innerPanel.setBorder(BorderFactory.createEmptyBorder(200, 100, 200, 100));
		
		menu.add(innerPanel);
		
		// navigate to single player game (probably go to level options)
		p1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				menu.setVisible(false);
				levelMenu.setVisible(true);
			}
		});
		
		// navigate to multi player game (probably go to level options)
		p2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				menu.setVisible(false);
				// open the multi player game
			}
		});
		
		// navigate to the help menu
		hp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				menu.setVisible(false);
				showHelpMenu();
			}
		});
		
		// navigate to the options menu
		op.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				options.setVisible(true);
				menu.setVisible(false);
				setSound(2);
			}
		});
		
		// quit the game
		qt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				menu.dispose();
				System.exit(0);
			}
		});
		
		backOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				options.setVisible(false);
				menu.setVisible(true);
			}
			
		});
		
		backQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				options.dispose();
				System.exit(0);
			}
		});
		
		level1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				levelMenu.setVisible(false);
				// open level 1
				Pong p = new Pong();
				
			}
		});
		
		level2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(getLevelProgress() > 1)
				{
					levelMenu.setVisible(false);
					// open level 2
				}
			}
		});
		
		level3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(getLevelProgress() > 2)
				{
					levelMenu.setVisible(false);
					// open level 3
			
				}
			}
		});
		
		level4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(getLevelProgress() > 3)
				{
					levelMenu.setVisible(false);
					// open level 3
				}
			}
		});
		
		level5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(getLevelProgress() > 4)
				{	
					levelMenu.setVisible(false);
					// open level 3
				}
			}
		});
		
		level6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(getLevelProgress() > 5)
				{
					levelMenu.setVisible(false);
					// open level 3
				}
			}
		});
		
		backLevels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				levelMenu.setVisible(false);
				menu.setVisible(true);
			}
		});
		
		quitLevels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				levelMenu.dispose();
				System.exit(0);
			}
		});
		
		menu.setDefaultCloseOperation(menu.EXIT_ON_CLOSE);
		options.setDefaultCloseOperation(options.EXIT_ON_CLOSE);
		levelMenu.setDefaultCloseOperation(levelMenu.EXIT_ON_CLOSE);
		help.setDefaultCloseOperation(help.EXIT_ON_CLOSE);
		
		menu.setVisible(true);
	}
	
	public void showHelpMenu()
	{
		
		help.setVisible(true);

		JPanel helpPanel = new JPanel(new BorderLayout());
		JPanel helpGrid = new JPanel(new GridLayout(2, 4, 10, 10));
		
		JPanel helpTop = new JPanel();
		JButton helpTitle = new JButton("HELP");
		helpTitle.setFont(new Font("Verdana", 1, 20));
		helpTop.add(helpTitle);
		helpTop.setBorder(new LineBorder(Color.BLACK));
		help.add(helpTop);
		helpTop.setVisible(true);
		
		JButton helpTitleText = new JButton("HELP SCREEN");
		helpTitleText.setBorder(new LineBorder(Color.black));
		helpTitleText.setOpaque(false);
		helpTitleText.setContentAreaFilled(false);
		helpTitleText.setBorderPainted(false);
		helpTitleText.setFont(new Font("Verdana", Font.PLAIN, 25));
		
		JButton item1 = new JButton("item1.png");
		JButton item1Desc = new JButton("<html>Item 1 Description is that<br> is does things");
		JButton item2 = new JButton("item2.png");
		JButton item2Desc = new JButton("<html>Item 2 Description is that<br> is does things");
		JButton item3 = new JButton("item3.png");
		JButton item3Desc = new JButton("<html>Item 3 Description is that<br> is does things");
		JButton item4 = new JButton("item4.png");
		JButton item4Desc = new JButton("<html>Item 4 Description is that<br> is does things");
		JButton item5 = new JButton("item5.png");
		JButton item5Desc = new JButton("<html>Item 5 Description is that<br> is does things");
		JButton item6 = new JButton("item6.png");
		JButton item6Desc = new JButton("<html>Item 6 Description is that<br> is does things");
		
		JPanel helpInner = new JPanel(new GridLayout(6, 2, 0, 20));
		helpInner.add(item1).setLocation(0, 0);
		helpInner.add(item1Desc).setLocation(0, 1);
		item1Desc.setOpaque(false);
		item1Desc.setContentAreaFilled(false);
		item1Desc.setBorderPainted(false);
		helpInner.add(item2).setLocation(1, 0);
		helpInner.add(item2Desc).setLocation(1, 1);
		helpInner.add(item3).setLocation(2, 0);
		helpInner.add(item3Desc).setLocation(2, 1);
		helpInner.add(item4).setLocation(3, 0);
		helpInner.add(item4Desc).setLocation(3, 1);
		helpInner.add(item5).setLocation(4, 0);
		helpInner.add(item5Desc).setLocation(4, 1);
		helpInner.add(item6).setLocation(5, 0);
		helpInner.add(item6Desc).setLocation(5, 1);
		helpInner.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
		
		/*
		helpInner.add(Box.createRigidArea(new Dimension(5, 1)));
		helpInner.add(item1).setLocation(0, 0);
		helpInner.add(item1Desc).setLocation(0, 1);
		helpInner.add(item2).setLocation(1, 0);
		helpInner.add(item2Desc).setLocation(1, 1);
		helpInner.add(item3).setLocation(2, 0);
		helpInner.add(item3Desc).setLocation(2, 1);
		helpInner.add(item4).setLocation(3, 0);
		helpInner.add(item4Desc).setLocation(3, 1);
		helpInner.add(item5).setLocation(4, 0);
		helpInner.add(item5Desc).setLocation(4, 1);
		helpInner.add(item6).setLocation(5, 0);
		helpInner.add(item6Desc).setLocation(5, 1);
		helpInner.setBorder(BorderFactory.createEmptyBorder(200, 400, 200, 400));
		*/
		help.add(helpInner);
		
		JLabel instLabel = new JLabel("<html>"
				+ "HOW TO PLAY<br>"
				+ "The goal of the game is to out-survive, and "
				+ "out-score your opponent.<br>"
				+ "To do this, control your paddle to deflect the ball away from your base. <br>"
				+ "Watch out for items though! While some can help you, others will do damage, ensuring "
				+ "a quick victory for your opponent.<br> "
				+ ""
				+ "</html>");
		
		//helpPanel.add(instLabel);
		
	
		/*
		// setting up the main menu
		JPanel top = new JPanel();
		JLabel title = new JLabel("Breakout Ball");
		title.setFont(new Font("Verdana",1, 20));
		top.add(title);
		top.setBorder(new LineBorder(Color.BLACK));
		menu.add(top);
		top.setVisible(true);
		//menu.add(top);
		
		// title of game
		titleText = new JButton("Breakout Ball");
		titleText.setBorder(new LineBorder(Color.black));
		titleText.setOpaque(false);
		titleText.setContentAreaFilled(false);
		titleText.setBorderPainted(false);
		titleText.setFont(new Font("Verdana", Font.PLAIN, 25));
		
		// buttons for menu navigation
		p1 = new JButton("1 Player");
		p1.setBorder(new LineBorder(Color.BLACK));
		p1.setBackground(Color.WHITE);
		p1.setOpaque(true);
		p2 = new JButton("2 Player");
		p2.setBorder(new LineBorder(Color.BLACK));
		p2.setBackground(Color.WHITE);
		p2.setOpaque(true);
		op = new JButton("Options");
		op.setBorder(new LineBorder(Color.BLACK));
		op.setBackground(Color.WHITE);
		op.setOpaque(true);
		hp = new JButton("Help");
		hp.setBorder(new LineBorder(Color.BLACK));
		hp.setBackground(Color.WHITE);
		hp.setOpaque(true);
		qt = new JButton("Quit");
		qt.setBorder(new LineBorder(Color.BLACK));
		qt.setBackground(Color.WHITE);
		qt.setOpaque(true);	
		// adding proper bits into main panel
		JPanel innerPanel = new JPanel(new GridLayout(0, 1, 50, 0));
		innerPanel.add(titleText);
		innerPanel.add(Box.createRigidArea(new Dimension(5, 10)));
		innerPanel.add(p1);
		innerPanel.add(p2);
		innerPanel.add(op);
		innerPanel.add(hp);
		innerPanel.add(qt);
		innerPanel.setOpaque(false);
		innerPanel.setBorder(BorderFactory.createEmptyBorder(200, 100, 200, 100));
		
		menu.add(innerPanel);
		 */
		
		
		
		/*
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
        
        /*
         * 				HELP MENU
         * Navigation
         * 		space - multiplayer
         * 		shift - single player
         * 		a/d or left/right arrows to choose settings
         * 
         * Paddle
         * 		Player 1 - W (up), S (down)
         * 		Player 2 - Up Arrow (up), Down Arrow (down)
         * Close Help
         * 		h
         * 
         */
	}
	
	public static void setSound(int level)
	{
		int currentLevel = 0;
		if(currentLevel == level)
			return;
		if(currentLevel == 2 && level == 1)
		{
			
		}
	}
	
	// creating new image
	private static ImageIcon createImageIcon(String path, String description)
	{
		java.net.URL imgURL = Menu.class.getResource(path);
		if(imgURL != null)
		{
			return new ImageIcon(imgURL, description);
		}
		else 
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public int getLevelProgress()
	{
		return levelsComplete;
	}
	
}
