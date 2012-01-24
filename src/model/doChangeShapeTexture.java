package model;


public class doChangeShapeTexture implements Command
{
	String textureName;
	int whichElement;
	
	public doChangeShapeTexture( String textureName, int whichElement  )
	{
		this.textureName = textureName;
		this.whichElement = whichElement;
	}
	
	public void invoke(EditorMap map) throws CommandInvokeException 
	{
		MapShape shape = map.getShape( whichElement );
		String oldTexture = shape.getTextureName( );		
		shape.setTextureName( textureName );
		textureName = oldTexture;
	}

	public void undo(EditorMap map) throws CommandUndoException 
	{
		try {
			invoke(map);
		} catch (CommandInvokeException e) {
			e.printStackTrace();
		}
	}	
}