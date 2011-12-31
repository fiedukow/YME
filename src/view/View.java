package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.EventObject;
import java.util.concurrent.BlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import controller.Tool;
import controller.ViewState;
import controller.event.Event;

import model.EditorMap;

public class View {
	ViewState currentState;
	BlockingQueue<Event> eventQueue;
	JFrame mainFrame;
	MapPanel mapPanel;
	ToolPanel toolbox;
	NavigatePanel navigatePanel;
	MainMenu menuBar;
	JPanel leftMenu;	
	
	JPanel attributes;
	
	JPanel questions;
	JPanel statusBar;
	JTextArea statusText;
	JComboBox objectSelected;
	public View( ViewState state, BlockingQueue<Event> events )
	{			
		currentState = state;
		mainFrame 	 = new JFrame("YME :: new map");
		mapPanel  	 = new MapPanel( this );
		toolbox  	 = new ToolPanel( this );
		navigatePanel= new NavigatePanel( this );
		menuBar		 = new MainMenu( this );
				
		leftMenu  	 = new JPanel();		
		attributes 	 = new JPanel();
		statusBar 	 = new JPanel();
		eventQueue	 = events;
		questions 	 = new JPanel();
		Container cp = mainFrame.getContentPane();
		
		
		mainFrame.setPreferredSize(new Dimension(1024,768));
		mainFrame.setJMenuBar(menuBar);	
		
		
		
		statusText = new JTextArea("Debug window:\n",5,100);		
		statusText.setLineWrap(true);
		JScrollPane statusTextScroll = new JScrollPane(statusText);
		statusTextScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		statusBar.add( statusTextScroll );	
		statusBar.setLayout(new GridLayout(1,1));
					
		leftMenu.setLayout(new BorderLayout());
		leftMenu.add(toolbox, BorderLayout.NORTH);
		//leftMenu.add(attributes, BorderLayout.CENTER);
		leftMenu.add(navigatePanel, BorderLayout.CENTER);
		questions.setPreferredSize(new Dimension(200, 200));
		leftMenu.add(questions, BorderLayout.SOUTH);
		
				
		JLabel textureNameLabel = new JLabel("Tekstura: ");
		JTextField textureName = new JTextField(10);
		JButton textureFind = new JButton("...");
		textureFind.setPreferredSize(new Dimension(30, 18));
		questions.add(textureNameLabel);
		questions.add(textureName);
		questions.add(textureFind);
		
		
		
		
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.repaint();
        cp.setLayout(new BorderLayout());
        cp.add(mapPanel, BorderLayout.CENTER);
        cp.add(leftMenu, BorderLayout.EAST);
        cp.add(statusBar, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
        System.out.println("View created.");      
	}
	
	public JFrame getFrame()
	{
		return mainFrame;
	}
	
	public void showInfo( String toShow )
	{
		statusText.append(toShow);
		statusText.setCaretPosition( statusText.getText().length() );
	}
	
	public void setCurrentState( ViewState map )
	{
		this.currentState = map;	
		mapPanel.repaint( );		
		toolbox.repaint( );
		navigatePanel.repaint( );
	}
	
	public void pushEvent( Event event )
	{
		try
		{
			this.eventQueue.put(event);
		}
		catch ( InterruptedException e )
		{
			this.showInfo( e.getMessage() );
		}
	}
	
	ViewState getState()
	{
		return currentState;
	}
}