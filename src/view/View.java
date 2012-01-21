package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import controller.question.ViewQuestion;

import model.EditorMap;

/**
 * 
 * 
 * @author fiedukow
 *
 */
public class View {
	ViewState currentState;
	BlockingQueue<Event> eventQueue;
	
	JFrame mainFrame;
	JPanel leftMenu;	
	MapPanel mapPanel;
	ToolPanel toolbox;
	NavigatePanel navigatePanel;
	MainMenu menuBar;
	JPanel statusBar;	
	JTextArea statusText;
	
	/*temporary only*/
	JPanel questions;
		
	
	
	public View( ViewState state, BlockingQueue<Event> events )
	{				
		currentState = state;
		eventQueue	 = events;
		
		Container contentPane;
													
		{
			mainFrame 	 = new JFrame("YME :: new map"); /*TODO, frame name should be MapName, change it in repaint()*/						
			mapPanel  	 = new MapPanel( this );
			leftMenu     = new JPanel( );
			statusBar 	 = new JPanel( );
			
			navigatePanel= new NavigatePanel( this );
			menuBar		 = new MainMenu( this );				
			toolbox  	 = new ToolPanel( this );		
			
			statusText   = new JTextArea( "Debug window:\n", 5, 100 );
			questions 	 = new QuestionPanel( this );
			
			contentPane  = mainFrame.getContentPane();
		}	
							
		
		{					
			statusText.setLineWrap( true );			
			JScrollPane statusTextScroll = new JScrollPane( statusText );
			statusTextScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);			
			statusBar.add( statusTextScroll );	
			statusBar.setLayout(new GridLayout(1,1)); //TODO, something better?
		}
				
		
		{			
			leftMenu.setLayout(new BorderLayout());
			leftMenu.add(toolbox, BorderLayout.NORTH);		
			leftMenu.add(navigatePanel, BorderLayout.CENTER);			
			leftMenu.add(questions, BorderLayout.SOUTH);
		}
							
		
		{
			contentPane.setLayout(new BorderLayout());
			contentPane.add(mapPanel, BorderLayout.CENTER);
			contentPane.add(leftMenu, BorderLayout.EAST);
			contentPane.add(statusBar, BorderLayout.SOUTH);
		}
		
		{
			mainFrame.setPreferredSize(new Dimension(1024,768));
			mainFrame.setJMenuBar(menuBar);
	        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.pack();
			mainFrame.repaint();
			mainFrame.setVisible(true);
		}
		
        System.out.println("View created.");      
	}
	
	public JFrame getFrame()
	{
		return mainFrame;
	}
	

	public synchronized void showInfo( String toShow )
	{
		statusText.append(toShow);
		statusText.setCaretPosition( statusText.getText().length() );
	}

	//TODO, invoke later here?
	public synchronized void setCurrentState( ViewState map )
	{
		this.currentState = map;	
		mapPanel.repaint( );		
		toolbox.repaint( );
		navigatePanel.repaint( );
		leftMenu.remove(questions);
		questions = new QuestionPanel(this);
		leftMenu.add(questions, BorderLayout.SOUTH);
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
