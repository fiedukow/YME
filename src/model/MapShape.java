package model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Vector;


/**
 * Base class describing every single shape possible to represent on map.
 * @author fiedukow
 */
abstract class MapShape
{
	private String textureName;	
	private TypeOfMapObject typeOfObject; /** Current value type of map object */	
	protected Shape shapeObject; /**The reference to the basic shape object (created in derived class constructor)*/
	/*TODO universal attribute collection?*/
	
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
