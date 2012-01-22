package model;

public class doRemoveShape implements Command
{	
	MapShape shape;
	int index;

	public doRemoveShape( int index )
	{
		this.index = index;
	}
	
	public void invoke( EditorMap map ) throws CommandInvokeException
	{
		if( shape == null ) shape = map.getShapes().get(index);
		map.removeMapShape( shape );		 
	}
	public void undo( EditorMap map ) throws CommandUndoException
	{
		map.addMapShape(shape);
	}	
}