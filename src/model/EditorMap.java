package model;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import com.thoughtworks.xstream.XStream;

/**
 * The map description for map Editor. Have some get/set function as interface.
 * @author fiedukow
 */
final class EditorMap
{
	private String mapName;					/*Map name, for general proposes*/
	private String waterTexture;			/*The texture used as the background*/
	private Vector<MapPolygon> polygons;	/*List of polygons presented on the map*/
	
	public EditorMap( String mapName, String waterTexture, Vector<MapPolygon> polygons )
	{
		this.mapName = mapName;
		this.waterTexture = waterTexture;
		this.polygons = polygons;
	}
	
	/**
	 * Simple setter
	 * @param mapName
	 */
	public void setMapName( String mapName )
	{
		this.mapName = mapName;
	}
	
	/**
	 * Simple getter
	 * @return
	 */
	public String getMapName( )
	{
		return this.mapName;
	}
	
	/**
	 * Simple setter
	 * @param waterTexture
	 */
	public void setWaterTexture( String waterTexture )
	{
	    this.waterTexture = waterTexture;
	}

	/**
	 * Simple getter
	 * @return
	 */
	public String getWaterTexture( )
	{
	    return this.waterTexture;
	}
	
	/**
	 * @return the polygons
	 */
	public Vector<MapPolygon> getPolygons() {
		return polygons;
	}

	/**
	 * Adds polygon to the map.
	 * @param poly
	 */
	public void addMapPolygon( MapPolygon poly )
	{
		getPolygons().add(poly);
	}
	
	/**
	 * Removes polygon from map.
	 * @param id index of the one that should be removed
	 */
	public void removeMapPolygon( int id )
	{
		getPolygons().remove(id);
	}

}

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
		/*TODO getters, setters?*/
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
			MapPolygon current = new MapPolygon( pol.getTextureName() );
			for( Point pt : pol.getPoints() )
			{
				current.addPoint((int)pt.getX(), (int)pt.getY());
			}
			resultPolygons.add(current);
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

/**
 * XMLPolygon ready to serialize using XStream. Only for save/load proposes. 
 * @author fiedukow
 */
class XMLPolygon
{
	private String textureName; /* name of the texture */
	private Vector<Point> vertices = new Vector<Point>();
	
	/**
	 * Main constructor - creates XMLPolygon using MapPolygon
	 * @param polygon
	 */
	public XMLPolygon( MapPolygon polygon )
	{
		for( int i = 0; i < polygon.xpoints.length; ++i )
		{
			vertices.add( new Point(polygon.xpoints[i], polygon.ypoints[i]) );
		}
		textureName = polygon.textureName;		
	}
	
	/**
	 * Simple getter
	 * @return
	 */
	public Vector<Point> getPoints()
	{
		return vertices;
	}
	
	/**
	 * Simple setter
	 * @return
	 */
	public String getTextureName()
	{
		return textureName;
	}
}

/**
 * MapPolygon - main (& atomic) element of EditorMap, using java.awt.Polygon as base 
 * @author fiedukow
 */
final class MapPolygon extends Polygon
{
	String textureName; /* name of the texture file */
	HashMap< String, Object > attributes;
	/**
	 * Initialize empty polygon with selected texture
	 * @param textureName
	 */
	MapPolygon( String textureName )
	{
		super();
		this.textureName = textureName;
	}
	
	/**
	 * Simple seter
	 * @param textureName
	 */
	void setTextureName ( String textureName )
	{
		this.textureName = textureName;
	}
	
	/**
	 * Simple geter
	 * @param textureName
	 * @return
	 */
	String getTextureName ( String textureName )
	{
		return textureName;
	}	
	
	/**
	 * Resize polygon for the given Bounds size.
	 * @param h - new height of polygon bounds
	 * @param w - new width of polygon bounds
	 */
	void resizePolygon( int h, int w )
	{
		Rectangle bound = getBounds();
		double widthFactor = w/bound.getWidth();
		double heightFactor = h/bound.getHeight();
		for( int i = 0; i < xpoints.length; ++i )
			xpoints[i] *= widthFactor;
		for( int i = 0; i < ypoints.length; ++i )
			ypoints[i] *= heightFactor;
	}
	
	/**
	 * Move polygon to place where first (upper left) corner of it's bounds is in given place. 
	 * @param x x coordinate for upper left corner of polygon bounds
	 * @param y y coordinate for upper left corner of polygon bounds
	 */
	public void movePolygon( int x, int y )
	{
		Rectangle bound = getBounds();
		int xmove = x - (int)bound.getMinX();
		int ymove = y - (int)bound.getMinY();
		for( int i = 0; i < xpoints.length; ++i )
			xpoints[i] += xmove;
		for( int i = 0; i < ypoints.length; ++i )
			ypoints[i] += ymove;
	}
	
	/**
	 * Allow to change polygon attribute (but not to create new one!). It check if the type of new object is the same then the old one.
	 * @param key
	 * @param value
	 * @throws AttributeTypeMatchException throw when the type of new Object isn't same as the old one.
	 * @throws AttributeNotFoundException throw when someone try to change unexisting value 
	 */
	void changeAttribute( String key, Object value ) throws AttributeTypeMatchException, AttributeNotFoundException
	{
		if( ! attributes.containsKey(key) )
		{
			throw new AttributeNotFoundException();
		}
		if( value.getClass() != attributes.get(key).getClass() )
		{
			throw new AttributeTypeMatchException();
		}
		attributes.put( key, value );						
	}
	
	/**
	 * Allow to add attribute (but not to overwrite old one!).
	 * @param key
	 * @param value
	 * @throws AttributeOverwriteException throw when someone try to add value to the key with is already in use.
	 */
	void addAttribute( String key, Object value ) throws AttributeOverwriteException
	{
		if( attributes.containsKey(key) )
			throw new AttributeOverwriteException();
		attributes.put( key, value );
	}
}

/*
 * 
 * 		EXCEPTION CLASSES BELOW
 *  
 */

class AttributeTypeMatchException extends Exception
{	
}
class AttributeNotFoundException extends Exception
{	
}
class AttributeOverwriteException extends Exception
{	
}

