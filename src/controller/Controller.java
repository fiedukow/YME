package controller;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import controller.event.*;
import controller.question.*;

import model.Command;
import model.CommandStackEmptyException;
import model.MapShape;
import model.Model;
import model.TypeOfMapObject;
import view.View;

/*Controller in MVC meaning*/
public class Controller extends Thread 
{
	Model model;
	View view;
	ViewState viewState;
	BlockingQueue< Event > events;	
	static final HashMap < Class < ? extends Event >, Action > actionMap = new HashMap < Class < ? extends Event >, Action >();
	
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
	}
	
	
	/**
	 * A little bit ugly question generator for current focus
	 * It generates all question controller should ask view.
	 * TODO, It's hardcoded because it's constant for every type of FocusTape
	 * Maybe it be coded in classes like Map & Shape, and here only decide
	 * which class will be asked for questions to ask.   
	 * @return 
	 */
	ArrayList<ViewQuestion> generateQuestionsForCurrentFocus()
	{
		ArrayList<ViewQuestion> questions = new ArrayList<ViewQuestion>();
		
		try{			
			switch( viewState.getFocusType() )
			{
				case MAP:
					//current attributes values
					questions.add( new StringValueQuestion("mapName",QuestionType.STRING, getMapName() ) );
					questions.add( new StringValueQuestion("texture",QuestionType.STRING, getWaterTexture() ) );				
					break;
				case START_POINT:
					//current attributes values
					int x = getStartPointX();
					int y = getStartPointY();				
					questions.add( new DoubleIntValueQuestion("position",QuestionType.TWICE_INT, x,y ) );							
					break;				
				case SHAPE:
					//current attributes values
					MapShape currentShape = getShapeById( getFocusId() );
					int pos[] = currentShape.getPosition();
					int siz[] = currentShape.getSize();
					TypeOfMapObject currentTOMP = currentShape.getTypeOfObject();					
					LinkedList<TypeOfMapObject> possibleTOMP = currentShape.getAllowedTypesOfMapObject();					
					String texture = currentShape.getTextureName();
					
					questions.add( new StringValueQuestion("texture",QuestionType.STRING, texture ) );
					questions.add( new DoubleIntValueQuestion("position",QuestionType.TWICE_INT, pos[0], pos[1] ) );					
					questions.add( new DoubleIntValueQuestion("size",QuestionType.TWICE_INT, siz[0], siz[1] ) );
					questions.add( new TypeOfMapObjectQuestion("typeOfMapObject", QuestionType.TYPE_OF_MAP_OBJECT, currentTOMP, possibleTOMP) );
					questions.add( new ActionQuestion("delete",QuestionType.BUTTON ) );					
					break;
				default:
					break;
			}		
		}
		catch( WrongQuestionTypeException e )
		{			
			System.err.println("Niemozliwy wyjatek w kontrolerze, wymagana rewizja kodu.");
			e.printStackTrace(); /*impossible statement*/
		}
		return questions;
	}	
	
	
	
	
	private void refresh()
	{
		//there is no better place for this:
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
		refresh();
	}
	
}