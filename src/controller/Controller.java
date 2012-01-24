package controller;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import controller.event.*;
import controller.question.*;

import model.Command;
import model.CommandStackEmptyException;
import model.EditorMap;
import model.MapShape;
import model.Model;
import view.View;

/**
 * Controller in MVC meaning.
 * @author fiedukow
 *
 */
public class Controller extends Thread 
{
	Model model;
	View view;
	ViewState viewState;
	BlockingQueue< Event > events;	
	static final HashMap < Class < ? extends Event >, Action > actionMap = new HashMap < Class < ? extends Event >, Action >();
	
	/**
	 * Connect events with concrete actions
	 */
	static
	{
		actionMap.put( EventPointSelect.class 		, new ActionSelectPoint() 	);
		actionMap.put( EventPointAccept.class 		, new ActionPointAccept() 	);
		actionMap.put( EventToolSelect.class  		, new ActionToolSelect()  	);
		actionMap.put( EventUndo.class        		, new ActionUndo()        	);
		actionMap.put( EventRedo.class        		, new ActionRedo()        	);
		actionMap.put( EventChangeFocus.class 		, new ActionChangeFocus() 	);
		actionMap.put( EventLoadMap.class     		, new ActionLoadMap()     	);
		actionMap.put( EventSaveMap.class     		, new ActionSaveMap()     	);
		actionMap.put( EventNewMap.class           	, new ActionNewMap()      	);
		actionMap.put( EventExitProgram.class      	, new ActionExit()        	);
		actionMap.put( EventQuestionAnswered.class 	, new ActionQuestionAnswer());
	}

	public Controller ( Model model, View view, BlockingQueue<Event> events)
	{
		this.model = model;
		this.view = view;
		this.events = events;		
		System.out.println("Controller created!");
		viewState = new ViewState(model.getEditorMap(), model.getToolbox());
		view.setCurrentState( viewState );
		start();
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
				System.err.println("Kontroler umarl :-(");
				throw new RuntimeException();
			}
		}
	}
	
	/*
	 * Internal accessors for other package Controller classes
	 */
	
	Tool getSelectedTool()
	{
		return viewState.getSelectedTool();
	}
	
	FocusType getFocusType()
	{
		return viewState.getFocusType();
	}
	
	Integer getFocusId()
	{
		return viewState.getFocusId();
	}
	
	public void setSelectedTool(Tool tool) {
		viewState.setSelectedTool( tool );
	}
	
	int getStartPointRange()
	{
		return viewState.getStartPointRange();
	}
	
	void setFocus( FocusType focusType )
	{
		viewState.setFocus( focusType );
	}
	
	void setFocus( FocusType focusType, Integer focusId )
	{
		viewState.setFocus( focusType, focusId );
	}
	
	
	ArrayList<MapShape> getMapShapes()
	{
		 return model.getEditorMap().getShapes();
	}
	
	void addPoint( Point toAdd )
	{
		viewState.getPolygonBuffer( ).addPoint( toAdd );
	}
	
	void doCommand( Command toDo )
	{
		model.getToolbox().doCommand( toDo );
	}
	
	int getStartPointX( )
	{
		return (int) viewState.getMap().getStartPoint().getX();
	}
	
	int getStartPointY( )
	{
		return (int) viewState.getMap().getStartPoint().getY();
	}
	
	ArrayList<Point> getBufferedPolygonVerticles() throws ToFewVerticlesException
	{
		return viewState.getPolygonBuffer().getPolygonVerticles();
	}
	
	void resetPolygonBuffer()
	{
		viewState.getPolygonBuffer().reset();
	}
	
	void showInfo( String toShow )
	{
		view.showInfo( toShow );
	}
	
	void toolboxUndo() throws CommandStackEmptyException
	{
		model.getToolbox().undo();
	}
	
	void toolboxRedo() throws CommandStackEmptyException
	{
		model.getToolbox().redo();
	}
	
	int getMapShapesCount()
	{
		return viewState.getMap().getShapes().size();
	}
	
	MapShape getShapeById( int id )
	{
		return getMapShapes().get(id);
	}
	
	String getMapName()
	{
		return model.getEditorMap().getMapName();
	}
	
	String getWaterTexture()
	{
		return model.getEditorMap().getWaterTexture();
	}
	
	void loadMap( String filePath ) throws FileNotFoundException
	{
		model.loadMap( filePath );
	}
	
	void saveMap( String filePath ) throws IOException
	{
		model.saveMap( filePath );
	}
	
	void newMap( )
	{
		model.newMap();	
	}
	
	EditorMap getMap()
	{
		return model.getEditorMap();
	}
	
	void createAndSetNewViewState( )
	{
		this.viewState = new ViewState( model.getEditorMap(), model.getToolbox() );
		view.setCurrentState(viewState);	
	}
		
	void doAfterDraw()
	{
		viewState.setFocus( 
				viewState.getSelectedTool() == Tool.STARTPOINT ? FocusType.START_POINT : FocusType.SHAPE, 
				viewState.getMap().getShapes().size()-1
				);
		viewState.setSelectedTool(Tool.SELECTOR);
		refreshView();
	}
	
	
	/**
	 * A little bit ugly question generator for current focus
	 * It generates all question controller should ask view.
	 * @return ArrayLisit of question for current focus - ready to set on ViewState
	 */
	ArrayList<ViewQuestion> generateQuestionsForCurrentFocus()
	{
		switch( viewState.getFocusType() )
		{
			case MAP:
				return getMap().getMapQuestions();
			case START_POINT:
				return getMap().getStartPointQuestions();								
			case SHAPE:
				return getMap().getShapeQuestions( getFocusId() );
			default:
				break;
		}
		return null;		
	}	
	
	private void refreshView()
	{
		if( viewState.getSelectedTool() != Tool.POLYGON )
			viewState.getPolygonBuffer().reset();
		viewState.setQuestion( generateQuestionsForCurrentFocus() );
		view.setCurrentState( viewState );
	}
	
	private void doEvent( Event event )
	{
		Action toDo = actionMap.get(event.getClass());
		if( actionMap.get(event.getClass()) != null )
			toDo.invoke( this, event );
		refreshView();
	}
	
}