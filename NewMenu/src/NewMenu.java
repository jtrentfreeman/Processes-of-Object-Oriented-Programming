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

	// frames
	private JFrame menu;
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
	private JButton backHelp;		// go back to ...			 help
	private JButton mText;			// music text
	private JButton mOn;			// music icon 3 bars
	private JButton mLow;			// music icon 1 bar
	private JButton mOff;			// music icon 0 bars
	private JButton sText;			// sound text
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
		menu.setSize(WIDTH, (int) (WIDTH*1.5));
		
		options = new JFrame("Options");
		options.setSize(WIDTH, (int) (WIDTH*1.5));
		
		help = new JFrame("Help");
		help.setSize(WIDTH, (int) (WIDTH*1.5));
		
		// grabs the images from NewMenu/src/resources (and also held in NewMenu/bin/resources) (For Eclipse)
		ImageIcon mOnIcon = new ImageIcon("/resources/musicHigh.png");
		ImageIcon mLowIcon = new ImageIcon("/resources/musicLow.png");
		ImageIcon mOffIcon = new ImageIcon("/resources/musicOff.png");
			
		// set the icons to their buttons
		mText = new JButton("Music Volume:");
		mOn = new JButton(mOnIcon);
		mOn.setVisible(true);
		mLow = new JButton(mOnIcon);
		mLow.setVisible(true);
		mOff = new JButton(mOffIcon);
		mOff.setVisible(true);

		// setting up the music option buttons
		JLabel mOnL = new JLabel(mOnIcon);
		mOnL.setIcon(mOnIcon);
		mOnL.setOpaque(true);
		mOnL.setBorder(new LineBorder(Color.BLACK));
		mOnL.setBackground(Color.WHITE);
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
		
		// soundPanel will hold the music and sound buttons (off, low, high)
		JPanel soundPanel = new JPanel(new GridLayout(4, 2, 0, 0));
		options.add(soundPanel);
		
		// adding proper bits into the frame
		// still doesn't show icons, not sure why
		soundPanel.add(Box.createRigidArea(new Dimension(5, 10)));
		soundPanel.add(mOnL);
		soundPanel.add(mLowL);
		soundPanel.add(mOffL);
		soundPanel.setOpaque(false);
		soundPanel.setVisible(true);
		soundPanel.setBorder(BorderFactory.createEmptyBorder(200, 100, 200, 100));	
		
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
				menu.dispose();
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
				menu.dispose();
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
		
		options.setDefaultCloseOperation(options.EXIT_ON_CLOSE);
		menu.setDefaultCloseOperation(menu.EXIT_ON_CLOSE);
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
	
}
