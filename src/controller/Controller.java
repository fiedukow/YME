package controller;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.concurrent.BlockingQueue;

import controller.event.Event;
import controller.event.EventChangeFocus;
import controller.event.EventLoadMap;
import controller.event.EventNewMap;
import controller.event.EventPointAccept;
import controller.event.EventPointSelect;
import controller.event.EventRedo;
import controller.event.EventSaveMap;
import controller.event.EventToolSelect;
import controller.event.EventUndo;

import model.MapShape;
import model.Model;
import model.TypeOfMapObject;
import model.doDrawEllipse;
import model.doDrawPolygon;
import model.doDrawRectangle;
import model.doSetStartPoint;
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
		/*try {
			model.loadMap("maps/sample.xml");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			model.saveMap("maps/sample1.xml");
		} 
		catch (IOException e) 
		{
			System.err.println("Nie udalo sie zapisac mapy!");
		}
		*/
		viewState = new ViewState(model.getEditorMap(), model.getToolbox());
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
			//view.showInfo("Event handled\n");
		}
	}
	
	private void doEvent( EventPointAccept event )
	{
		if( viewState.getSelectedTool() == Tool.POLYGON )
			try {
				model.getToolbox().doCommand( new doDrawPolygon("brick.jpg", viewState.getPolygonBuffer().getPolygonVerticles(), null) );
				viewState.getPolygonBuffer().reset();			
				doAfterDraw();
			} catch (ToFewVerticlesException e) {
				view.showInfo("Zaznaczono zbyt malo punktow by utworzyc polygon\n");
				viewState.getPolygonBuffer().reset();
			}
	}
	
	void doAfterDraw()
	{
		viewState.setFocus( 
				viewState.getSelectedTool()==Tool.STARTPOINT ? FocusType.START_POINT : FocusType.SHAPE, 
				viewState.getMap().getShapes().size()-1
				);
		viewState.setSelectedTool(Tool.SELECTOR);
	}
	
	private void doEvent( EventPointSelect event )
	{
		
		int x,y;
		x = event.getX();
		y = event.getY();
		if( viewState.getSelectedTool() == Tool.POLYGON )
			viewState.getPolygonBuffer().addPoint( new Point(x, y) );
		else if( ! (viewState.getSelectedTool() == Tool.SELECTOR) )			
		{
			if( viewState.getSelectedTool() == Tool.RECTANGLE )
				model.getToolbox().doCommand(new doDrawRectangle("wood.jpg", x,y,40,40,null));			
			if( viewState.getSelectedTool() == Tool.ELLIPSE )
				model.getToolbox().doCommand(new doDrawEllipse("red.jpg", x,y,40,40,null));		
			if( viewState.getSelectedTool() == Tool.QUEY )
				model.getToolbox().doCommand(new doDrawRectangle("metal.jpg", x,y,200,30,TypeOfMapObject.QUAY));						
			if( viewState.getSelectedTool() == Tool.STARTPOINT )
				model.getToolbox().doCommand(new doSetStartPoint(x,y));

			doAfterDraw();
			return;
			

		}
		
		else
		{
			int i = 0;
			boolean focusFound = false;
			int distanceFromStart = 
					(int) (
						Math.sqrt(
								Math.pow(x-viewState.getMap().getStartPoint().getX(),2) +
								Math.pow(y-viewState.getMap().getStartPoint().getY(),2)
						)
					);
			if( distanceFromStart <= viewState.getStartPointRange() )
			{
				viewState.setFocus(FocusType.START_POINT);
				focusFound = true;
			}
			if( !focusFound )
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
	
	private void doEvent( EventUndo event )
	{
		try {
			model.getToolbox().undo();
			viewState.setFocus(FocusType.MAP);
		} catch (Exception e) {
			// TODO should be CommandStackEmptyException
			view.showInfo("Nie ma juz operacji do cofniecia");
		}
	}
	
	private void doEvent( EventRedo event )
	{
		try {
			model.getToolbox().redo();
		} catch (Exception e) {
			// TODO should be CommandStackEmptyException
			view.showInfo("Nie ma juz operacji do ponownienia");
		}
	}
	
	private void doEvent( EventChangeFocus event )
	{
		Integer idFocus = event.getFocusId();
		if( idFocus!=null )
		{
		idFocus = idFocus%viewState.getMap().getShapes().size();
		if( idFocus < 0 )
			idFocus = event.getFocusId()%viewState.getMap().getShapes().size() + idFocus;
		
		}
		viewState.setFocus(event.getFocusType(), event.getFocusId());
		
	}
	
	private void doEvent( EventLoadMap event )
	{
		try
		{
			model.loadMap(event.getFilePath());
			viewState = new ViewState( model.getEditorMap(), model.getToolbox() );
			view.setCurrentState(viewState);	
		} catch ( FileNotFoundException e ) {
			view.showInfo("Nie udalo sie otworzyc pliku... Szczegoly: "+ e.getMessage() +"\n");			
		}
		
	}
	
	private void doEvent( EventSaveMap event )
	{
		try {
			model.saveMap(event.getFilePath());
		} catch (IOException e) {
			view.showInfo("Nie udalo sie zapisac pliku... Szczegoly: "+ e.getMessage() +"\n");
		}		
	}
	
	
	private void doEvent( EventNewMap event )
	{
		view.showInfo("Zaczynamy prace nad nową mapą\n");
		model.newMap();
		viewState = new ViewState( model.getEditorMap(), model.getToolbox() );
		view.setCurrentState(viewState);	
	}	
	
	private void doEvent( Event event )
	{
		switch( event.getEventType() )
		{
			case MAP_POINT_SELECT:
				doEvent( (EventPointSelect) event  );
				break;
			case MAP_POINT_ACCEPT:
				doEvent( (EventPointAccept) event );
				break;
			case TOOL_SELECT:
				doEvent( (EventToolSelect) event );
				break;
			case UNDO:
				doEvent( (EventUndo) event );
				break;
			case REDO:
				doEvent( (EventRedo) event );
				break;
			case CHANGE_FOCUS:
				doEvent( (EventChangeFocus) event );
				break;
			case LOAD_MAP:
				doEvent( (EventLoadMap) event );
				break;
			case SAVE_MAP:
				doEvent( (EventSaveMap) event );
				break;
			case NEW_MAP:
				doEvent( (EventNewMap) event );
				break;
			case EXIT_PROGRAM:
				System.exit(0);
				break;
			default:
				break;
		}		
		//TODO italian solution?
		if( viewState.getSelectedTool() != Tool.POLYGON )
			viewState.getPolygonBuffer().reset();
		view.setCurrentState( viewState );
	}
}