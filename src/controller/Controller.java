package controller;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
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
import controller.question.ViewQuestion;
import controller.question.WrongQuestionTypeException;

import model.MapShape;
import model.Model;
import model.TypeOfMapObject;
import model.doChangeMapName;
import model.doChangeWaterTexture;
import model.doDrawEllipse;
import model.doDrawPolygon;
import model.doDrawRectangle;
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
				view.showInfo("Kontroler umarl :-(");
				return;
			}
		}
	}
	
	
	//TODO, all doEvent to Events classes
	//TODO, default texture & size should be given by model, not by controller
	private void doEvent( EventPointAccept event )
	{
		if( viewState.getSelectedTool() == Tool.POLYGON )
			try {
				model.getToolbox().doCommand
						( 
								new doDrawPolygon("brick.jpg", viewState.getPolygonBuffer().getPolygonVerticles(), null) 
						);
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
					int pos[] = this.model.getEditorMap().getShapes().get(viewState.getFocusId()).getPosition();
					int siz[] = this.model.getEditorMap().getShapes().get(viewState.getFocusId()).getSize();
					String texture = this.model.getEditorMap().getShapes().get(viewState.getFocusId()).getTextureName(); 
					questions.add( new StringValueQuestion("texture",QuestionType.STRING, texture ) );
					questions.add( new DoubleIntValueQuestion("position",QuestionType.TWICE_INT, pos[0], pos[1] ) );
					questions.add( new DoubleIntValueQuestion("size",QuestionType.TWICE_INT, siz[0], siz[1] ) );
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
	
	private void doEvent( EventPointSelect event )
	{
		
		int x,y;
		x = event.getX();
		y = event.getY();
		if( viewState.getSelectedTool() == Tool.POLYGON )
			viewState.getPolygonBuffer().addPoint( new Point(x, y) );
		else if( ! (viewState.getSelectedTool() == Tool.SELECTOR) )			
		{
			//TODO, switch here
			if( viewState.getSelectedTool() == Tool.RECTANGLE )
				model.getToolbox().doCommand( new doDrawRectangle( "wood.jpg", x, y, 40, 40, null ) );			
			if( viewState.getSelectedTool() == Tool.ELLIPSE )
				model.getToolbox().doCommand( new doDrawEllipse( "red.jpg", x, y, 40, 40, null ) );		
			if( viewState.getSelectedTool() == Tool.QUEY )
				model.getToolbox().doCommand( new doDrawRectangle( "metal.jpg", x, y, 200, 30, TypeOfMapObject.QUAY ) );						
			if( viewState.getSelectedTool() == Tool.STARTPOINT )
				model.getToolbox().doCommand( new doSetStartPoint(x,y) );

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
			/*TODO, do internal focus change event to avoid copy of the code*/
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
			view.showInfo("Nie ma juz operacji do cofniecia\n");
		}
	}
	
	private void doEvent( EventRedo event )
	{
		try {
			model.getToolbox().redo();
		} catch (Exception e) {
			// TODO should be CommandStackEmptyException
			view.showInfo("Nie ma juz operacji do ponownienia\n");
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
			case QUESTION_ANSWERED:
				doEvent( (EventQuestionAnswered) event );
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
		
		viewState.setQuestion( generateQuestionsForCurrentFocus() );
		view.setCurrentState( viewState );
		/*TODO This is sleep only for testing proposes, delete it in release version*/
		/*
		try {
			sleep(500);
		} catch (InterruptedException e) {
			System.err.println("Ktos mnie budzi:(");
		}*/
	}
}