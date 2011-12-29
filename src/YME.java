import java.io.IOException;
import java.util.EventObject;
import java.util.concurrent.ArrayBlockingQueue;

import view.View;

import controller.Controller;
import controller.ViewState;
import controller.event.Event;
import model.Model;

/*Main class*/
public class YME {
	public static final void main( String[] args )
	{		
		System.out.println("Witaj w YME :-)");		
		Model md = new Model();
		ArrayBlockingQueue<Event> bq = new ArrayBlockingQueue<Event>(128); 
		View v = new View( new ViewState(md.getEditorMap()), bq );
		new Controller(md,v,bq);
	}
}