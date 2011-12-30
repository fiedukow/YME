package controller;

import java.io.IOException;

import java.util.concurrent.BlockingQueue;

import controller.event.Event;
import controller.event.EventPointSelect;
import controller.event.EventToolSelect;

import model.MapShape;
import model.Model;
import model.doDrawEllipse;
import model.doDrawRectangle;
import view.View;

/*Controller in MVC meaning*/
public class Controller extends Thread 
{
	Model model;
	View view;
	ViewState viewState;
	BlockingQueue<Event> events;
	
	public Controller ( Model model, View view, BlockingQueue<Event> events)
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
				doEvent( events.take() );			
			} catch (InterruptedException e) {
				/*
				 * something goes terribly horribly wrong (or its program end?)
				 */
				view.showInfo("Kontroler umarl :-(");
				return;
			}
			view.showInfo("Event handled\n");
		}
	}
	
	private void doEvent( EventPointSelect event )
	{
		
		int x,y;
		x = event.getX();
		y = event.getY();
		
		if( viewState.getSelectedTool() == Tool.RECTANGLE )
		{
			/*default rectangle*/
			model.getToolbox().doCommand(new doDrawRectangle("wood.jpg", x,y,40,40));
			viewState.setFocus(FocusType.SHAPE, viewState.getMap().getShapes().size()-1);
			viewState.setSelectedTool(Tool.SELECTOR);
			return;
		}
		
		if( viewState.getSelectedTool() == Tool.ELLIPSE )
		{
			/*default rectangle*/
			model.getToolbox().doCommand(new doDrawEllipse("red.jpg", x,y,40,40));
			viewState.setFocus(FocusType.SHAPE, viewState.getMap().getShapes().size()-1);
			viewState.setSelectedTool(Tool.SELECTOR);
			return;
		}
		
		if( viewState.getSelectedTool() == Tool.SELECTOR )
		{
			int i = 0;
			boolean focusFound = false;
			for( MapShape shape : model.getEditorMap().getShapes())
			{
				if( shape.getShapeObject().contains(x, y) )
				{
					viewState.setFocus(FocusType.SHAPE, i);
					focusFound = true;
					//TODO maybe backward and brake?
				} 				
				++i;
			}
			if( !focusFound ) viewState.setFocus(FocusType.MAP);			
			
			return;
		}
	}
	
	private void doEvent( EventToolSelect event )
	{		
		viewState.setSelectedTool(event.getTool());
		view.setCurrentState( viewState );
	}
	
	
	private void doEvent( Event event )
	{
		switch( event.getEventType() )
		{
			case MAP_POINT_SELECT:
				doEvent( (EventPointSelect) event  );
				break;
			case TOOL_SELECT:
				doEvent( (EventToolSelect) event);
				break;
			default:
				break;
		}		
		view.setCurrentState( viewState );
	}
}