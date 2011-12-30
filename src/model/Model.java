package model;

import java.awt.Point;
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
		newMap("New map", "water.jpg", new Point(25,25));		
		box = new EditorToolbox(map);
		System.out.println("Model created.");
	}
	
	public EditorToolbox getToolbox()
	{
		return box;
	}
	
	public void newMap( String mapName, String textureName, Point startPoint )
	{
		map = new EditorMap( mapName, textureName, new Vector<MapShape>(), startPoint );		
	}
	
	public void loadMap( String fileName ) throws FileNotFoundException
	{
		MapTranslator XMLMap;
		XMLMap = new MapTranslator( fileName );
		map = XMLMap.translate();
		box = new EditorToolbox(map);
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