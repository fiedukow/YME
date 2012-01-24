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
		shape = map.getShape(index);
		map.removeMapShape( shape );		 
	}
	public void undo( EditorMap map ) throws CommandUndoException
	{
		map.addMapShape( index, shape );
	}	
}