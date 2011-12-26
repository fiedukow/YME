package model;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
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
 */
final class EditorMap
{
	String mapName;					/*Map name, for general proposes*/
	String waterTexture;			/*The texture used as the background*/
	Vector<MapPolygon> polygons;	/*List of polygons presented on the map*/
	EditorMap( String mapName, String waterTexture, Vector<MapPolygon> polygons )
	{
		this.mapName = mapName;
		this.waterTexture = waterTexture;
		this.polygons = polygons;
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
	String mapName;					/*Map name, for general proposes*/
	String waterTexture;			/*The texture used as the background*/
	Vector<XMLPolygon> polygons;	/*List of polygons presented on the map*/	

	public static final String xmlHeader = "<?xml version=\"1.0\"?>\n";
	
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
		tmp = (MapTranslator) xstream.fromXML( new File(fileName) );
		this.mapName = tmp.mapName;
		this.waterTexture = tmp.waterTexture;
		this.polygons = tmp.polygons;		
	}	

	/**
	 * Use EditorMap object to create current map state
	 * @param Map - map to create MapTranslator object
	 */
	public MapTranslator( EditorMap Map )
	{	
		/*FIXME*/				
	}
	
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
	String textureName; /* name of the texture */
	Vector<Point> points;
	XMLPolygon( MapPolygon polygon )
	{
		for( int i = 0; i < polygon.xpoints.length; ++i )
			points.add( new Point(polygon.xpoints[i], polygon.ypoints[i]) );
		textureName = polygon.textureName;		
	}
	Vector<Point> getPoints()
	{
		return points;
	}
	String getTextureName()
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
		
	String textureName; /* name of the texture */
	HashMap< String, Object > attributes;
	void setTextureName ( String textureName )
	{
		this.textureName = textureName;
	}
	String getTextureName ( String textureName )
	{
		return textureName;
	}	
	MapPolygon( String textureName )
	{
		this.textureName = textureName;
	}
	/**
	 * Resize polygon for the given Bounds size.
	 * @param h - new height of polygon bounds
	 * @param w - new width of polygon bounds
	 */
	void resizePolygon( int h, int w )
	{
		Rectangle bound = getBounds();
		double widthFactor = bound.getWidth()/w;
		double heightFactor = bound.getHeight()/h;
		for( int i = 0; i < xpoints.length; ++i )
			xpoints[i] *= widthFactor;
		for( int i = 0; i < ypoints.length; ++i )
			ypoints[i] *= heightFactor;
	}
	
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
	void addAttribute( String key, Object value ) throws AttributeOverwriteException
	{
		if( attributes.containsKey(key) )
			throw new AttributeOverwriteException();
		attributes.put( key, value );
	}
}


class AttributeTypeMatchException extends Exception
{	
}
class AttributeNotFoundException extends Exception
{	
}
class AttributeOverwriteException extends Exception
{	
}
