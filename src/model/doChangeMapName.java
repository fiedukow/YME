package model;


public class doChangeMapName implements Command
{
	String mapName;
	
	public doChangeMapName( String mapName )
	{
		this.mapName = mapName;
	}
	
	public void invoke(EditorMap map) throws CommandInvokeException {
		String oldTexture = map.getMapName();
		map.setMapName( mapName );
		mapName = oldTexture;
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
