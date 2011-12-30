package model;

public class doDrawEllipse implements Command
{	
	MapEllipse ellipse;
	
	public doDrawEllipse( String textureName, int x, int y, int w, int h )
	{
		this.ellipse = new MapEllipse( textureName, x,y,w,h);		
	}
	
	public void invoke( EditorMap map ) throws CommandInvokeException
	{
		map.addMapShape( ellipse );
	}
	public void undo( EditorMap map ) throws CommandUndoException
	{
		map.removeMapShape( ellipse );
	}
}