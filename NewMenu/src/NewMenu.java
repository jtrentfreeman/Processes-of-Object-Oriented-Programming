import java.io.*;
import java.util.*;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.*;


// still ugly in terms of color and design
// not sure how to add background or pictures, if somebody wants to look into that
public class NewMenu {

	int levelsComplete = 0;
	
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
	
	NewMenu()
	{
		setUp();
	}
	
	public static void main(String[] args)
	{		
		NewMenu nMenu = new NewMenu();	
	}
	
	// sets up each menu at once, need to split up into individual methods
	private void setUp()
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
//		ImageIcon mOnIcon = new ImageIcon(getClass().getResource("/resources/musicHigh.png"));
//		ImageIcon mLowIcon = new ImageIcon(getClass().getResource("/resources/musicLow.png"));
//		ImageIcon mOffIcon = new ImageIcon(getClass().getResource("/resources/musicOff.png"));
			
		// set the icons to their buttons
		mOn = new JButton(mOnIcon);
		mOn.setVisible(true);
		mLow = new JButton(mLowIcon);
		mLow.setVisible(true);
		mOff = new JButton(mOffIcon);
		mOff.setVisible(true);
		
		sOn = new JButton("sOnIcon");
		sOn.setVisible(true);
		sLow = new JButton("sLowIcon");
		sLow.setVisible(true);
		sOff = new JButton("sOffIcon");
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
		menu.add(top);
		
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
				// open the single player game
			}
		});
		
		// navigate to multi player game (probably go to level options)
		p2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				menu.dispose();
				// open the multi player game
			}
		});
		
		// navigate to the help menu
		hp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				help.setVisible(true);
			}
		});
		
		// navigate to the options menu
		op.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				options.setVisible(true);
				menu.setVisible(false);
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
