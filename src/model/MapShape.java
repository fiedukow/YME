package model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
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
		allowedTypes = new Vector<TypeOfMapObject>();
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
	
	abstract public void resize(int w, int h);
	abstract public void move(int x, int y);
	abstract protected void loadAllowedTypes();
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
	
	public MapPolygon()
	{
		super(""); /**FIXME*/
		shapeObject = new Polygon();
		loadAllowedTypes();
	}
	
	public MapPolygon( String textureName )
	{
		super( textureName );
		loadAllowedTypes();
		this.shapeObject = new Polygon();
		try
		{
			this.setTypeOfObject( allowedTypes.get(0));
		}
		catch ( InvalidTypeOfMapObjectException e )
		{
			/*
			 * Impossible statement
			 */
			System.err.println("Blad aplikacji :-(");
		}
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

	
	/**
	 * Resize polygon for the given Bounds size.
	 * @param h - new height of polygon bounds
	 * @param w - new width of polygon bounds
	 */
	public void resize( int w, int h )
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
	public void move( int x, int y )
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
	
	
	/*
	 * PRIVATE SECTION
	 */
	/**
	 * @return Self shape object in proper type
	 */
	private Polygon getPolygon()
	{
		return (Polygon) shapeObject;
	}
	
	protected void loadAllowedTypes()
	{		
		allowedTypes.add( TypeOfMapObject.DESTROY );
		allowedTypes.add( TypeOfMapObject.STOP );
		allowedTypes.add( TypeOfMapObject.BUMP );
	}
}


/*TODO Javadoc for the class below */
/**
 * MapPolygon - main (& atomic) element of EditorMap, using java.awt.Polygon as base 
 * @author fiedukow
 */
final class MapRectangle extends MapShape
{
	public MapRectangle()
	{
		super(""); /**FIXME*/
		shapeObject = new Rectangle();
		loadAllowedTypes();
	}
	
	
	/**
	 * Initialize empty polygon with selected texture
	 * @param textureName
	 */
	public MapRectangle( String textureName, int x, int y, int w, int h )
	{
		super( textureName );
		this.shapeObject = new Rectangle(x,y,w,h);
		loadAllowedTypes();		
		try
		{
			this.setTypeOfObject( allowedTypes.get(0));
		}
		catch ( InvalidTypeOfMapObjectException e )
		{
			/*
			 * Impossible statement
			 */
			System.err.println("Blad aplikacji :-(");
		}
	}
	
	/**
	 * Resize polygon for the given Bounds size.
	 * @param h - new height of polygon bounds
	 * @param w - new width of polygon bounds
	 */
	public void resize( int w, int h )
	{
		getRectangle().setSize(w, h);
	}
	
	/**
	 * Move polygon to place where first (upper left) corner of it's bounds is in given place. 
	 * @param x x coordinate for upper left corner of polygon bounds
	 * @param y y coordinate for upper left corner of polygon bounds
	 */
	public void move( int x, int y )
	{
		getRectangle().setLocation(x, y);
	}
	
	public int getX()
	{
		return (int) getRectangle().getX();
	}
	
	public int getY()
	{
		return (int) getRectangle().getY();
	}
	
	public int getW()
	{
		return (int) getRectangle().getWidth();
	}
	
	public int getH()
	{
		return (int) getRectangle().getHeight();
	}
	
	public void setRectangle( int x, int y, int w, int h )
	{
		getRectangle().setRect(x, y, w, h);
	}
	
	
	/*
	 * PRIVATE SECTION
	 */
	/**
	 * @return Self shape object in proper type
	 */
	private Rectangle getRectangle()
	{
		return (Rectangle) shapeObject;
	}
	
	protected void loadAllowedTypes()
	{		
		allowedTypes.add( TypeOfMapObject.DESTROY );
		allowedTypes.add( TypeOfMapObject.STOP );
		allowedTypes.add( TypeOfMapObject.BUMP );
		allowedTypes.add( TypeOfMapObject.QUAY );
	}
}



/*TODO Javadoc for the class below */
/**
 * MapPolygon - main (& atomic) element of EditorMap, using java.awt.Polygon as base 
 * @author fiedukow
 */
final class MapEllipse extends MapShape
{
	
	public MapEllipse()
	{
		super(""); /**FIXME*/
		shapeObject = new Ellipse2D.Double();
		loadAllowedTypes();
	}
	
	/**
	 * Initialize empty polygon with selected texture
	 * @param textureName
	 */
	public MapEllipse( String textureName, int x, int y, int w, int h )
	{
		super( textureName );
		loadAllowedTypes();
		this.shapeObject = new Ellipse2D.Double(x,y,w,h);
		try
		{
			this.setTypeOfObject( allowedTypes.get(0));
		}
		catch ( InvalidTypeOfMapObjectException e )
		{
			/*
			 * Impossible statement
			 */
			System.err.println("Blad aplikacji :-(");
		}
	}
	
	/**
	 * Resize polygon for the given Bounds size.
	 * @param h - new height of polygon bounds
	 * @param w - new width of polygon bounds
	 */
	public void resize( int w, int h )
	{
		getEllipse().setFrame(getX(), getY(), w, h);
	}
	
	/**
	 * Move polygon to place where first (upper left) corner of it's bounds is in given place. 
	 * @param x x coordinate for upper left corner of polygon bounds
	 * @param y y coordinate for upper left corner of polygon bounds
	 */
	public void move( int x, int y )
	{
		getEllipse().setFrame(x, y, getW(), getH());
	}
	
	public int getX()
	{
		return (int) getEllipse().getX();
	}
	
	public int getY()
	{
		return (int) getEllipse().getY();
	}
	
	public int getW()
	{
		return (int) getEllipse().getWidth();
	}
	
	public int getH()
	{
		return (int) getEllipse().getHeight();
	}
	
	public void setEllipse( int x, int y, int w, int h )
	{
		getEllipse().setFrame(x, y, w, h);
	}
	
	
	/*
	 * PRIVATE SECTION
	 */
	/**
	 * @return Self shape object in proper type
	 */
	private Ellipse2D.Double getEllipse()
	{
		return (Ellipse2D.Double) shapeObject;
	}
	protected void loadAllowedTypes()
	{		
		allowedTypes.add( TypeOfMapObject.DESTROY );
		allowedTypes.add( TypeOfMapObject.STOP );
		allowedTypes.add( TypeOfMapObject.BUMP );
	}
}
