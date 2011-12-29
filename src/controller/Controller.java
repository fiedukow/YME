package controller;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import model.Model;
import view.View;

/*Controller in MVC meaning*/
public class Controller extends Thread 
{
	Model model;
	View view;
	ViewState viewState;
	BlockingQueue<Integer> events;
	
	public Controller ( Model model, View view )
	{
		this.model = model;
		this.view = view;
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
		start();
		
		view.showInfo("Kontroler wita widok :-)\n");
		
	}
	public void run()
	{
		viewState.setFocus(FocusType.SHAPE, 2);
		view.setCurrentState( viewState );
		try{
			sleep(3000);
		}
		catch( Exception e )
		{			
			System.err.println(e.getMessage());
		}
		viewState.setFocus(FocusType.SHAPE, 3);
		view.setCurrentState( viewState );
		/*
		while( true )
		{
			try {
				doEvent( events.take() );
			} catch (InterruptedException e) {
								
			}			
		}*/
	}
	
	private void doEvent( Integer i /*TODO should be event type*/)
	{
		
	}
}