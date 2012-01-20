import java.util.concurrent.ArrayBlockingQueue;

import view.View;

import controller.Controller;
import controller.ViewState;
import controller.event.Event;
import model.Model;


/*
 * Overall TODO list
 *  - Exception handling
 *  - Java doc in controller and model
 *  - View State class(?)
 *  - Questions in view
 *  - Access modifiers
 */


/*Main class*/
public class YME 
{
	public static final void main( String[] args )
	{		
		System.out.println("Witaj w YME :-)");		
		Model md = new Model();
		ArrayBlockingQueue<Event> bq = new ArrayBlockingQueue<Event>( 128 ); 
		View v = new View( new ViewState(md.getEditorMap(), md.getToolbox()), bq );
		new Controller(md,v,bq);
	}
}