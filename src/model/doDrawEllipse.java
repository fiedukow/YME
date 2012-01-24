package model;


public class doDrawEllipse implements Command
{	
	private MapEllipse ellipse;
	Integer id;
	
	public doDrawEllipse( String textureName, int x, int y, int w, int h, TypeOfMapObject type )
	{
		this.ellipse = new MapEllipse( textureName, x,y,w,h, type);		
	}
	
	public void invoke( EditorMap map ) throws CommandInvokeException
	{
		map.addMapShape( ellipse );
		id = map.getShapes().size()-1;
	}
	public void undo( EditorMap map ) throws CommandUndoException
	{
		ellipse = (MapEllipse) map.getShape( id );
		map.removeMapShape( id );
	}
}