package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import com.thoughtworks.xstream.XStream;


/**
 * Universal YahtMapFormat container. 
 * Have only objects with attributes saved in XML file. 
 * Can communicate with XML to initialize (but cannot be overloaded! 
 * You need to create new object instead) or to save state.
 */
final class MapTranslator
{
	private String mapName;					/*Map name, for general proposes*/
	private String waterTexture;			/*The texture used as the background*/
	private Vector<XMLPolygon> polygons;	/*List of polygons presented on the map*/

	public static final String xmlHeader = "<?xml version=\"1.0\"?>\n";
	
	/**
	 * Only for test, delete in release version.
	 */
	public MapTranslator()
	{
		this.polygons = new Vector<XMLPolygon>();
		this.mapName = "Something";
		this.waterTexture = "water.png";
		MapPolygon a = new MapPolygon("sniezek.png");
		a.addPoint(1, 1);
		a.addPoint(2, 1);
		a.addPoint(2, 2);
		a.addPoint(1, 2);
		polygons.add( new XMLPolygon(a) );
		a = new MapPolygon("boja.png");
		a.addPoint(1, 1);
		a.addPoint(2, 1);
		a.addPoint(2, 2);
		a.addPoint(1, 2);
		polygons.add( new XMLPolygon(a) );		
	}
	
	/**
	 * Load data from XML file using XStream.
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public MapTranslator( String fileName ) throws FileNotFoundException
	{
		MapTranslator tmp;
		XStream xstream = new XStream();		
		xstream.alias("map", MapTranslator.class);
		xstream.alias("polygon", XMLPolygon.class);
		xstream.alias("point", java.awt.Point.class);
		tmp = (MapTranslator) xstream.fromXML( new File(fileName) ); /*rewrite whole object*/
		this.mapName = tmp.mapName;
		this.waterTexture = tmp.waterTexture;
		this.polygons = tmp.polygons;		
	}	

	/**
	 * Use EditorMap object to create current map state
	 * @param Map - map to create MapTranslator object
	 */
	public MapTranslator( EditorMap map )
	{	
		this.mapName = map.getMapName();
		this.waterTexture = map.getWaterTexture();
		this.polygons = new Vector<XMLPolygon>();
		for( MapPolygon pol : map.getPolygons() )
		{	
			this.polygons.add( new XMLPolygon(pol) );
		}
	}
	
	/**
	 * Translate itself to the EditorMap format
	 * @return Generated EditorMap - main model Object
	 */
	public EditorMap translate()
	{		
		Vector<MapPolygon> resultPolygons = new Vector<MapPolygon>();		
		for( XMLPolygon pol : polygons )
		{
			resultPolygons.add(pol.translate());
		}
		EditorMap result = new EditorMap( mapName, waterTexture, resultPolygons );		
		return result;
	}
	
	
	/**
	 * Use XStreamer to save the map in XML file
	 * @param fileName - where do you want to save XMLFile?
	 * @throws IOException
	 */
	public final void save( String fileName ) throws IOException
	{
		XStream xstream = new XStream();
		xstream.alias("map", MapTranslator.class);
		xstream.alias("polygon", XMLPolygon.class);		
		xstream.alias("point", java.awt.Point.class);
		FileWriter xmlMap = new FileWriter( fileName );
		BufferedWriter writer = new BufferedWriter( xmlMap );
		try 
		{
			writer.write( xmlHeader );
			xstream.toXML(this, writer);
			writer.write("\n");
		} 
		catch ( IOException e )
		{
			System.err.println( "Blad zapisu do pliku!");
		}
		finally 
		{
		    writer.close();
		}
	} 
}
