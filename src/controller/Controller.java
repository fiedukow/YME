package controller;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import controller.event.Event;
import controller.event.EventChangeFocus;
import controller.event.EventLoadMap;
import controller.event.EventNewMap;
import controller.event.EventPointAccept;
import controller.event.EventPointSelect;
import controller.event.EventQuestionAnswered;
import controller.event.EventRedo;
import controller.event.EventSaveMap;
import controller.event.EventToolSelect;
import controller.event.EventUndo;
import controller.question.ActionQuestion;
import controller.question.DoubleIntValueQuestion;
import controller.question.QuestionType;
import controller.question.StringValueQuestion;
import controller.question.TypeOfMapObjectQuestion;
import controller.question.ViewQuestion;
import controller.question.WrongQuestionTypeException;

import model.Command;
import model.CommandStackEmptyException;
import model.MapShape;
import model.Model;
import model.TypeOfMapObject;
import model.doChangeMapName;
import model.doChangeTypeOfMapObject;
import model.doChangeWaterTexture;
import model.doMoveShape;
import model.doRemoveShape;
import model.doResizeShape;
import model.doSetStartPoint;
import model.doChangeShapeTexture;
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
		actionMap.put( EventPointSelect.class , new ActionSelectPoint() );
		actionMap.put( EventPointAccept.class , new ActionPointAccept() );
		actionMap.put( EventToolSelect.class  , new ActionToolSelect()  );
		actionMap.put( EventUndo.class        , new ActionUndo()        );
		actionMap.put( EventRedo.class        , new ActionRedo()        );
		actionMap.put( EventChangeFocus.class , new ActionChangeFocus() );
		actionMap.put( EventLoadMap.class     , new ActionLoadMap()     );
		actionMap.put( EventSaveMap.class     , new ActionSaveMap()     );
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
	
	void loadMap( String filePath ) throws FileNotFoundException
	{
		model.loadMap( filePath );
	}
	
	void saveMap( String filePath ) throws IOException
	{
		model.saveMap( filePath );
	}
	
	void createAndSetNewViewState( )
	{
		this.viewState = new ViewState( model.getEditorMap(), model.getToolbox() );
		view.setCurrentState(viewState);	
	}
	
	//TODO, all doEvent to Events classes
	//TODO, default texture & size should be given by model, not by controller
	
	void doAfterDraw()
	{
		viewState.setFocus( 
				viewState.getSelectedTool() == Tool.STARTPOINT ? FocusType.START_POINT : FocusType.SHAPE, 
				viewState.getMap().getShapes().size()-1
				);
		viewState.setSelectedTool(Tool.SELECTOR);
	}
	
	Vector<ViewQuestion> generateQuestionsForCurrentFocus()
	{
		Vector<ViewQuestion> questions = new Vector<ViewQuestion>();
		
		try{			
			switch( viewState.getFocusType() )
			{
				case MAP:
					questions.add( new StringValueQuestion("mapName",QuestionType.STRING, this.model.getEditorMap().getMapName() ) );
					questions.add( new StringValueQuestion("texture",QuestionType.STRING, this.model.getEditorMap().getWaterTexture() ) );				
					break;
				case START_POINT:
					int x = (int) this.model.getEditorMap().getStartPoint().getX();
					int y = (int) this.model.getEditorMap().getStartPoint().getY();				
					questions.add( new DoubleIntValueQuestion("position",QuestionType.TWICE_INT, x,y ) );							
					break;				
				case SHAPE:
					MapShape currentShape = this.model.getEditorMap().getShapes().get(viewState.getFocusId());
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
	
	
	private void doEvent( EventNewMap event )
	{
		view.showInfo("Zaczynamy prace nad nową mapą\n");
		model.newMap();
		viewState = new ViewState( model.getEditorMap(), model.getToolbox() );
		view.setCurrentState(viewState);	
	}
	
	private void doEvent( EventQuestionAnswered event )
	{
		ViewQuestion answer = event.getQuestion();			
		switch( viewState.getFocusType() )
		{
			case MAP:
				if( answer.getName() == "texture" )
				{
					String newTexture = ((StringValueQuestion) answer).getValue();
					this.model.getToolbox().doCommand( new doChangeWaterTexture( newTexture  ) );
				}
				else if( answer.getName() == "mapName" )
				{
					String newName = ((StringValueQuestion) answer).getValue();
					this.model.getToolbox().doCommand( new doChangeMapName( newName  ) );
				}
				break;
			case START_POINT:
				if( answer.getName() == "position" )
				{
					int pos[] = ((DoubleIntValueQuestion) answer).getValue();
					this.model.getToolbox().doCommand( new doSetStartPoint( pos[0], pos[1] ) );
				}
				break;				
			case SHAPE:		
				if( answer.getName() == "position" )
				{
					int pos[] = ((DoubleIntValueQuestion) answer).getValue();
					this.model.getToolbox().doCommand( new doMoveShape( pos[0], pos[1], viewState.getFocusId() ) );
				}
				else if( answer.getName() == "size" )
				{
					int size[] = ((DoubleIntValueQuestion) answer).getValue();
					this.model.getToolbox().doCommand( new doResizeShape( size[0], size[1], viewState.getFocusId() ) );
				}
				else if( answer.getName() == "texture" )
				{
					String newTexture = ((StringValueQuestion) answer).getValue();
					this.model.getToolbox().doCommand( new doChangeShapeTexture( newTexture, viewState.getFocusId() ) );
				}
				else if( answer.getName() == "typeOfMapObject" )
				{	
					TypeOfMapObject newType = ((TypeOfMapObjectQuestion) answer).getValue();
					this.model.getToolbox().doCommand( new doChangeTypeOfMapObject( newType, viewState.getFocusId() ) );				}
				else if( answer.getName() == "delete" )					
				{										
					this.model.getToolbox().doCommand( new doRemoveShape( viewState.getFocusId() ) );
					viewState.setFocus( FocusType.MAP );
				}
				break;			
			default:
				break;
		}				
	}	
	
	private void eventCleanup()
	{
		if( viewState.getSelectedTool() != Tool.POLYGON )
			viewState.getPolygonBuffer().reset();
	}
	
	private void doEvent( Event event )
	{
		Action toDo = actionMap.get(event.getClass());
		if( actionMap.get(event.getClass()) != null )
			toDo.invoke( this, event );
		
		switch( event.getEventType() )
		{				
			case NEW_MAP:
				doEvent( (EventNewMap) event );
				break;
			case QUESTION_ANSWERED:
				doEvent( (EventQuestionAnswered) event );
				break;
			case EXIT_PROGRAM:
				System.exit(0);
				break;
			default:
				break;
		}		
		eventCleanup();		
		
		viewState.setQuestion( generateQuestionsForCurrentFocus() );
		view.setCurrentState( viewState );
		/*TODO This is sleep only for testing proposes, delete it in release version*/
		/*try {
			sleep(500);
		} catch (InterruptedException e) {
			System.err.println("Ktos mnie budzi:(");
		}*/
	}
	
}