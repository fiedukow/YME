package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import controller.ViewState;
import controller.event.Event;

/**
 * Main class of the View
 * It contains and describes all GUI elements especially Main frame with all components inside
 * 
 * @author fiedukow
 *
 */
public class View {
	
	/*
	 * General informations
	 */
	/**
	 * Reference to current snapshot
	 */
	ViewState currentState;
	
	/**
	 * Reference to blocking queue which will be used to communicate with Controller
	 */
	BlockingQueue<Event> eventQueue;
	
	/*
	 * Graphics components 
	 */
	
	/**
	 * main window
	 */
	JFrame mainFrame;
	
	/**
	 * whole left part of the screen
	 */
	JPanel leftMenu;	
	
	/**
	 * Main part of the screen - map
	 */
	MapPanel mapPanel;
	
	/**
	 * Tools buttons with can be use to change mode
	 */
	ToolPanel toolbox;
	
	/**
	 * Panel which will be use to navigate between objects on map
	 * and to allow users to make undo/redo operations
	 */
	NavigatePanel navigatePanel;
	
	/**
	 * Main menu - contains most important functions of whole program
	 */
	MainMenu menuBar;
	
	/**
	 * Shows status information, especially debug window
	 */
	JPanel statusBar;	
	
	/**
	 * Textarea visible for user - to show him messages.
	 */
	JTextArea statusText;
	
	/**
	 * Panel with question from model (means - attributes to change for current focus)
	 */
	QuestionPanel questions;
		
	
	/**
	 * Main constructor of the view 
	 * @param state - initial state of the program (snapshot)
	 * @param events - blocking queue with should be used to send events to controller.
	 */
	public View( ViewState state, BlockingQueue<Event> events )
	{				
		currentState = state;
		eventQueue	 = events;	
																
		{ /*create all sub objects*/
			mainFrame 	 = new JFrame(""); // it will be set on first repaint 						
			mapPanel  	 = new MapPanel( this );
			leftMenu     = new JPanel( );
			statusBar 	 = new JPanel( );
			
			navigatePanel= new NavigatePanel( this );
			menuBar		 = new MainMenu( this );				
			toolbox  	 = new ToolPanel( this );		
			
			statusText   = new JTextArea( "Debug window:\n", 5, 100 );
			questions 	 = new QuestionPanel( this );
		}	
							
		
		{ /*create status bar*/
			statusText.setLineWrap( true );			
			JScrollPane statusTextScroll = new JScrollPane( statusText );
			statusTextScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);			
			statusBar.add( statusTextScroll );	
			statusBar.setLayout(new GridLayout(1,1)); //take all avalible space for one element
		}
				
		
		{ /*create left menu, add elements to it*/
			leftMenu.setLayout(new BorderLayout());
			leftMenu.add(toolbox, BorderLayout.NORTH);		
			leftMenu.add(navigatePanel, BorderLayout.CENTER);			
			leftMenu.add(questions, BorderLayout.SOUTH);
		}
							
		
		{ /*add things to contentPane, using BorderLayout*/
			Container contentPane = mainFrame.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(mapPanel, BorderLayout.CENTER);
			contentPane.add(leftMenu, BorderLayout.EAST);
			contentPane.add(statusBar, BorderLayout.SOUTH);
		}
		
		{ /*set up main window*/
			mainFrame.setPreferredSize(new Dimension(1350,850));
			mainFrame.setJMenuBar(menuBar);
	        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.pack();
			mainFrame.repaint();
			mainFrame.setVisible(true);
		}
		
        System.out.println("View created.");      
	}
	
	/**
	 * Accessor to frame 
	 * @return mainFrame - the main frame of Application
	 */
	public JFrame getFrame()
	{
		return mainFrame;
	}
	
	/**
	 * Show text in debug window - it is useful for debug and to show some informations for users.
	 * @param toShow - the String you want to show
	 */
	public void showInfo( String toShow )
	{
		statusText.append(toShow);
		statusText.setCaretPosition( statusText.getText().length() );
	}

	/**
	 * It update view using given snapshot.
	 * @param map - ViewState (snapshot of the appliaction model) - object containing all informations needed
	 */
	public void setCurrentState( ViewState map )
	{
		this.currentState = map;	
		mapPanel.repaint( );		
		toolbox.repaint( );
		navigatePanel.repaint( );
		mainFrame.setTitle( "YME :: "+map.getMap().getMapName() );
		if( !questions.acctual( getState().getFocusType(), getState().getFocusId()) )
		{
			leftMenu.remove(questions);
			questions = new QuestionPanel(this);		
			leftMenu.add(questions, BorderLayout.SOUTH);
		}
	}
	
	/**
	 * Adds event into Blocking Queue
	 * @param event - the event you want to push
	 */
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
	
	/**
	 * Simple getter
	 * @return reference to current snapshot.
	 */
	ViewState getState()
	{
		return currentState;
	}
}
