package controller;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.EventObject;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import model.MapShape;
import model.Model;
import view.View;

/*Controller in MVC meaning*/
public class Controller extends Thread 
{
	Model model;
	View view;
	ViewState viewState;
	BlockingQueue<EventObject> events;
	
	public Controller ( Model model, View view, BlockingQueue<EventObject> events)
	{
		this.model = model;
		this.view = view;
		this.events = events;
		System.out.println("Controller created!");
		model.loadMap("maps/sample.xml");
		try 
		{
			model.saveMap("maps/sample1.xml");
		} 
		catch (IOException e) 
		{
			System.err.println("Nie udalo sie zapisac mapy!");
		}
		
		viewState = new ViewState(model.getEditorMap());	
		view.setCurrentState( viewState );
		start();
		
		view.showInfo("Kontroler wita widok :-)\n");
		
	}
	public void run()
	{
		while( true )
		{
			try {
				doEvent(events.take());
			} catch (InterruptedException e) {
				/*
				 * something goes terribly horribly wrong (or its program end?)
				 */
				view.showInfo("Kontroler umarl :-(");
				return;
			}
			view.showInfo("Pobralem event!\n");
		}
	}
	
	private void doEvent( EventObject event )
	{
		MouseEvent me = ((MouseEvent) event);
		int x,y;
		x = me.getX();
		y = me.getY();
		int i = 0;
		for( MapShape shape : model.getEditorMap().getShapes())
		{
			if( shape.getShapeObject().contains(x, y) )
			{
				viewState.setFocus(FocusType.SHAPE, i);
				/*TODO maybe backward and brake?*/
			}
			++i;
		}
		view.setCurrentState( viewState );
	}
}