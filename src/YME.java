import java.util.concurrent.ArrayBlockingQueue;

import view.View;

import controller.Controller;
import controller.ViewState;
import controller.event.Event;
import model.Model;

/*Main class*/
public class YME 
{
	public static final void main( String[] args )
	{		
		System.out.println("Witaj w YME :-)");		
		Model model = new Model();
		ArrayBlockingQueue<Event> blockingQueue= new ArrayBlockingQueue<Event>( 128 ); 
		View view = new View( new ViewState(model.getEditorMap(), model.getToolbox()), blockingQueue );
		new Controller(model,view,blockingQueue);
	}
}