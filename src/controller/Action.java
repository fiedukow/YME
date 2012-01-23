package controller;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.CommandStackEmptyException;
import model.MapShape;
import model.TypeOfMapObject;
import model.doChangeMapName;
import model.doChangeShapeTexture;
import model.doChangeTypeOfMapObject;
import model.doChangeWaterTexture;
import model.doDrawEllipse;
import model.doDrawPolygon;
import model.doDrawRectangle;
import model.doMoveShape;
import model.doRemoveShape;
import model.doResizeShape;
import model.doSetStartPoint;
import controller.event.*;
import controller.question.DoubleIntValueQuestion;
import controller.question.StringValueQuestion;
import controller.question.TypeOfMapObjectQuestion;
import controller.question.ViewQuestion;

interface Action 
{
	abstract void invoke( Controller father, Event generalEvent );
}

class ActionToolSelect implements Action
{
	public void invoke(Controller father, Event generalEvent) {
			EventToolSelect event = (EventToolSelect) generalEvent;			
			father.setSelectedTool(event.getTool());			
	}	
}

class ActionUndo implements Action
{
	public void invoke( Controller father, Event generalEvent)
	{
		try 
		{
			father.toolboxUndo();
			father.setFocus(FocusType.MAP);
		} 
		catch (CommandStackEmptyException e) 
		{
			father.showInfo("Nie ma juz operacji do cofniecia\n");
		}
	}
}

class ActionRedo implements Action
{
	public void invoke( Controller father, Event generalEvent)
	{
		try {
			father.toolboxRedo();
		} catch (CommandStackEmptyException e) {
			father.showInfo("Nie ma juz operacji do ponownienia\n");
		}
	}
}

class ActionChangeFocus implements Action
{
	public void invoke(Controller father, Event generalEvent) {		
		EventChangeFocus event = (EventChangeFocus) generalEvent;
		Integer mapShapeCount = father.getMapShapesCount();
		Integer idFocus = event.getFocusId();
		if( idFocus!=null )
		{
			idFocus = idFocus%mapShapeCount;
			if( idFocus < 0 )
				idFocus = event.getFocusId()%mapShapeCount + idFocus;				
		}
		father.setFocus(event.getFocusType(), event.getFocusId());
	}
}


class ActionLoadMap implements Action
{
	public void invoke(Controller father, Event generalEvent )
	{
		EventLoadMap event = (EventLoadMap) generalEvent;
		try
		{
			father.loadMap(event.getFilePath());
			father.createAndSetNewViewState();			
		} 
		catch ( FileNotFoundException e ) 
		{
			father.showInfo("Nie udalo sie otworzyc pliku... Szczegoly: "+ e.getMessage() +"\n");			
		}
	}
}


class ActionSaveMap implements Action
{
	public void invoke(Controller father, Event generalEvent )
	{
		EventSaveMap event = (EventSaveMap) generalEvent;
		try {
			father.saveMap(event.getFilePath());
		} catch (IOException e) {
			father.showInfo("Nie udalo sie zapisac pliku... Szczegoly: "+ e.getMessage() +"\n");
		}		
	}
}

class ActionNewMap implements Action
{
	public void invoke(Controller father, Event generalEvent )
	{
		father.showInfo("Zaczynamy prace nad nową mapą\n");
		father.newMap();
		father.createAndSetNewViewState();	
	}
}

class ActionExit implements Action
{
	public void invoke(Controller father, Event generalEvent )
	{
		System.exit(0);
	}	
}

class ActionQuestionAnswer implements Action
{
	public void invoke(Controller father, Event generalEvent) {
		EventQuestionAnswered event = (EventQuestionAnswered) generalEvent;
		ViewQuestion answer = event.getQuestion();		
		switch( father.getFocusType() )
		{
			case MAP:
				if( answer.getName() == "texture" )
				{
					String newTexture = ((StringValueQuestion) answer).getValue();
					father.doCommand( new doChangeWaterTexture( newTexture  ) );
				}
				else if( answer.getName() == "mapName" )
				{
					String newName = ((StringValueQuestion) answer).getValue();
					father.doCommand( new doChangeMapName( newName  ) );
				}
				break;
			case START_POINT:
				if( answer.getName() == "position" )
				{
					int pos[] = ((DoubleIntValueQuestion) answer).getValue();
					father.doCommand( new doSetStartPoint( pos[0], pos[1] ) );
				}
				break;				
			case SHAPE:		
				if( answer.getName() == "position" )
				{
					int pos[] = ((DoubleIntValueQuestion) answer).getValue();
					father.doCommand( new doMoveShape( pos[0], pos[1], father.getFocusId() ) );
				}
				else if( answer.getName() == "size" )
				{
					int size[] = ((DoubleIntValueQuestion) answer).getValue();
					father.doCommand( new doResizeShape( size[0], size[1], father.getFocusId() ) );
				}
				else if( answer.getName() == "texture" )
				{
					String newTexture = ((StringValueQuestion) answer).getValue();
					father.doCommand( new doChangeShapeTexture( newTexture, father.getFocusId() ) );
				}
				else if( answer.getName() == "typeOfMapObject" )
				{	
					TypeOfMapObject newType = ((TypeOfMapObjectQuestion) answer).getValue();
					father.doCommand( new doChangeTypeOfMapObject( newType, father.getFocusId() ) );				}
				else if( answer.getName() == "delete" )					
				{										
					father.doCommand( new doRemoveShape( father.getFocusId() ) );
					father.setFocus( FocusType.MAP );
				}
				break;			
			default:
				break;
		}
	}	
}

class ActionSelectPoint implements Action
{
	public void invoke(Controller father, Event generalEvent) {
		EventPointSelect event = (EventPointSelect) generalEvent;
		Tool selectedTool =  father.getSelectedTool();
		int x,y;
		x = event.getX();
		y = event.getY();
		/*drawing*/
		if( selectedTool == Tool.POLYGON )
			father.addPoint( new Point(x, y) );
		else if( ! ( selectedTool == Tool.SELECTOR) )			
		{
			//TODO, switch here
			switch( selectedTool )
			{
				case RECTANGLE:
					father.doCommand( new doDrawRectangle( "wood.jpg", x, y, 40, 40, null ) );			
					break;
				case ELLIPSE:
					father.doCommand( new doDrawEllipse( "red.jpg", x, y, 40, 40, null ) );
					break;
				case QUEY:
					father.doCommand( new doDrawRectangle( "metal.jpg", x, y, 200, 30, TypeOfMapObject.QUAY ) );
					break;
				case STARTPOINT:
					father.doCommand( new doSetStartPoint(x,y) );
					break;
			}

			father.doAfterDraw();
			return;		
		}
		/*selecting*/
		else
		{
			int i = 0;
			boolean focusFound = false;
			int distanceFromStart = 
					(int) (
						Math.sqrt(
								Math.pow(x-father.getStartPointX(),2) +
								Math.pow(y-father.getStartPointY(),2)
						)
					);
			if( distanceFromStart <= father.getStartPointRange() )
			{
				father.setFocus(FocusType.START_POINT);
				focusFound = true;
			}
			if( !focusFound )
			for( MapShape shape : father.getMapShapes() )
			{
				if( shape.getShapeObject().contains(x, y) )
				{
					father.setFocus(FocusType.SHAPE, i);
					focusFound = true;
					//TODO maybe backward and brake?
				} 				
				++i;
			}
			if( !focusFound ) father.setFocus(FocusType.MAP);			
			/*TODO, do internal focus change event to avoid copy of the code*/
			return;
			}		
	}	
}


class ActionPointAccept implements Action
{
	public void invoke(Controller father, Event generalEvent) {
		if( father.getSelectedTool() == Tool.POLYGON )
		{
			try {
				father.doCommand
						( 
								new doDrawPolygon("brick.jpg", father.getBufferedPolygonVerticles(), TypeOfMapObject.DESTROY ) 
						);			
				father.doAfterDraw();
			} 
			catch (ToFewVerticlesException e) 
			{
				father.showInfo("Zaznaczono zbyt malo punktow by utworzyc polygon\n");				
			}
			finally
			{
				father.resetPolygonBuffer();
			}
		}
	}	
}
