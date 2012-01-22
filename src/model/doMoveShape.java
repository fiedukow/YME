package model;

public class doMoveShape implements Command 
{
	int x,y,whichElement;
	
	public doMoveShape( int x, int y, int whichElement )
	{
		this.x = x;
		this.y = y;		
		this.whichElement = whichElement;
	}	
		
	public void invoke(EditorMap map) throws CommandInvokeException {
		MapShape shape = map.getShapes().get(whichElement);
		int oldPosition[] = shape.getPosition();		
		shape.move(x, y);
		x = oldPosition[0];
		y = oldPosition[1];
	}

	public void undo(EditorMap map) throws CommandUndoException {
		try {
			invoke( map );
		} catch (CommandInvokeException e) {
			System.err.println("BLAD: Cofanie nie powiodlo sie w wyniku nieznanego bledu!");
			throw new RuntimeException();
		}
	}

}
