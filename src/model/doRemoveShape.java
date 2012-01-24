package model;


public class doRemoveShape implements Command
{	
	private MapShape shape;
	private final int index;

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