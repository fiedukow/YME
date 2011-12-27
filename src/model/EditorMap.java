package model;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
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

/**
 * Base class describing every single shape possible to represent on map.
 * @author fiedukow
 */
abstract class MapShape
{
	private String textureName;	
	private TypeOfMapObject typeOfObject; /** Current value type of map object */
	private HashMap<String, Object> attributes; /** Any other attributes */	
	protected Shape shapeObject; /**The reference to the basic shape object (created in derived class constructor)*/
	
	protected static Vector<TypeOfMapObject> allowedTypes; /** Contains list of types allowed on this particular MapShape derived class. */
	
	/**
	 * Default constructor, set textureName
	 * @param textureName
	 */
	public MapShape( String textureName )
	{
		this.textureName = textureName;
		typeOfObject = TypeOfMapObject.NO_OBJECT;
		shapeObject = null;
	}
	
	public String getTextureName( )
	{
		return textureName;
	}
	
	public void setTextureName( String textureName )
	{
		this.textureName = textureName;
	}
	
	public TypeOfMapObject getTypeOfObject()
	{
		return typeOfObject;
	}
	
	/**
	 * Sets typeOfMapObject
	 * @param type - one of allowed types for the particular type
	 * @throws InvalidTypeOfMapObjectException - throw when someone try to set type to the disallowed TOMO
	 */
	public void setTypeOfObject( TypeOfMapObject type ) throws InvalidTypeOfMapObjectException
	{
		if( ! allowedTypes.contains( type ) )
		{
			throw new InvalidTypeOfMapObjectException();			
		}
		this.typeOfObject = type;
	}
	
	/**
	 * Allow to change mapShape attribute (but not to create new one!). It check if the type of new object is the same then the old one.
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
			vertices.add( new Point(xpoints[i], ypoints[i]) );
		}
		textureName = polygon.textureName;		
	}
	
	/**
	 * Simple setter
	 * @return
	 */
	public String getTextureName()
	{
		return textureName;
	}
	
	public MapPolygon translate()
	{
		MapPolygon result = new MapPolygon( getTextureName() );
		for( Point pt : this.vertices )
		{
			result.addPoint((int)pt.getX(), (int)pt.getY());
		}
		return result;
	}
}

/**
 * MapPolygon - main (& atomic) element of EditorMap, using java.awt.Polygon as base 
 * @author fiedukow
 */
final class MapPolygon extends MapShape
{
	/**
	 * Initialize empty polygon with selected texture
	 * @param textureName
	 */
	public MapPolygon( String textureName )
	{
		super( textureName );
		this.shapeObject = new Polygon();
		allowedTypes.add( TypeOfMapObject.DESTROY );
		allowedTypes.add( TypeOfMapObject.STOP );
		allowedTypes.add( TypeOfMapObject.BUMP );		
	}
	
	public void addPoint( int x, int y )
	{		
		getPolygon().addPoint(x,y);
	}
	
	public int[] getXCoords()
	{
		return getPolygon().xpoints;
	}
	
	public int[] getYCoords()
	{
		return getPolygon().xpoints;
	}
	
	
	private Polygon getPolygon()
	{
		return (Polygon) shapeObject;
	}
	
	/**
	 * Resize polygon for the given Bounds size.
	 * @param h - new height of polygon bounds
	 * @param w - new width of polygon bounds
	 */
	public void resizePolygon( int h, int w )
	{
		Polygon poly = getPolygon();
		Rectangle bound = poly.getBounds();
		double widthFactor = w/bound.getWidth();
		double heightFactor = h/bound.getHeight();
		for( int i = 0; i < poly.xpoints.length; ++i )
			poly.xpoints[i] *= widthFactor;
		for( int i = 0; i < poly.ypoints.length; ++i )
			poly.ypoints[i] *= heightFactor;
	}
	
	/**
	 * Move polygon to place where first (upper left) corner of it's bounds is in given place. 
	 * @param x x coordinate for upper left corner of polygon bounds
	 * @param y y coordinate for upper left corner of polygon bounds
	 */
	public void movePolygon( int x, int y )
	{
		Polygon poly = getPolygon();
		Rectangle bound = poly.getBounds();
		int xmove = x - (int)bound.getMinX();
		int ymove = y - (int)bound.getMinY();
		for( int i = 0; i < poly.xpoints.length; ++i )
			poly.xpoints[i] += xmove;
		for( int i = 0; i < poly.ypoints.length; ++i )
			poly.ypoints[i] += ymove;
	}		
}

enum TypeOfMapObject {
    NO_OBJECT, QUAY, DESTROY, STOP, BUMP
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

class InvalidTypeOfMapObjectException extends Exception
{	
}
