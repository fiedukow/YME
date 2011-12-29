package model;

/**
 * 
 * @author fiedukow
 */
public class doDrawRectangle implements Command
{	
	MapRectangle rectangle;
	
	public doDrawRectangle( String textureName, int x, int y, int w, int h )
	{
		this.rectangle = new MapRectangle( textureName, x,y,w,h);		
	}
	
	public void invoke( EditorMap map ) throws CommandInvokeException
	{
		map.addMapShape( rectangle );
	}
	public void undo( EditorMap map ) throws CommandUndoException
	{
		map.removeMapShape(rectangle);
	}
}