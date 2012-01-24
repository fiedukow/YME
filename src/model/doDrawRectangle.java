package model;


/**
 * 
 * @author fiedukow
 */
public class doDrawRectangle implements Command
{	
	private MapRectangle rectangle;
	Integer id;
	
	public doDrawRectangle( String textureName, int x, int y, int w, int h, TypeOfMapObject type )
	{
		this.rectangle = new MapRectangle( textureName, x,y,w,h, type);
	}
	
	public void invoke( EditorMap map ) throws CommandInvokeException
	{
		map.addMapShape( rectangle );
		id = map.getShapes().size()-1;
	}
	public void undo( EditorMap map ) throws CommandUndoException
	{
		rectangle = (MapRectangle) map.getShape( id );
		map.removeMapShape( id );
	}
}