package model;

public class doChangeWaterTexture implements Command
{
	String textureName;
	
	public doChangeWaterTexture( String textureName )
	{
		this.textureName = textureName;
	}
	
	public void invoke(EditorMap map) throws CommandInvokeException {
		String oldTexture = map.getWaterTexture();
		map.setWaterTexture( textureName );
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
