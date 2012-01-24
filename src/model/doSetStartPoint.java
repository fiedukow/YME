package model;

import java.awt.Point;


public class doSetStartPoint implements Command 
{	
	int x,y;
	
	public doSetStartPoint( int x, int y )
	{
		this.x = x;
		this.y = y;
	}
	
	public void invoke( EditorMap map ) throws CommandInvokeException
	{
		int ox, oy;
		ox = (int) map.getStartPoint().getX();
		oy = (int) map.getStartPoint().getY();
		map.setStartPoint(new Point(x,y));
		x = ox;
		y = oy;
	}
	public void undo( EditorMap map ) throws CommandUndoException
	{
		/*it will reverse the spell :P */
		try
		{
			invoke( map );
		}
		catch ( CommandInvokeException e )
		{
			throw new CommandUndoException();
		}
	}	
}
