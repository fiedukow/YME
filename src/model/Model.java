package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/*
 * Model class in MVC meaning...
 */
public final class Model 
{
	private EditorMap map;
	private EditorToolbox box;
	public Model()
	{		
		newMap("New map", "water.jpg");
		box = new EditorToolbox(map);
		System.out.println("Model created.");
	}
	
	public EditorToolbox getToolbox()
	{
		return box;
	}
	
	public void newMap( String mapName, String textureName )
	{
		map = new EditorMap( mapName, textureName, new Vector<MapShape>() );		
	}
	
	public void loadMap( String fileName )
	{
		MapTranslator XMLMap;
		try {
			XMLMap = new MapTranslator( fileName );
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			return;
		}
		map = XMLMap.translate();
	}
	
	public void saveMap( String fileName ) throws IOException 
	{
		MapTranslator XMLMap = new MapTranslator( map );
		XMLMap.save(fileName);
	}
	
	public EditorMap getEditorMap()
	{
		return map;
	}
	/*FIXME - i need some interface ;( */
}