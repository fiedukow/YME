package model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Vector;


/**
 * Base class describing every single shape possible to represent on YME map.
 * @author fiedukow
 * @see java.awt.Shape
 */
abstract class MapShape
{
	private String textureName;	
	private TypeOfMapObject typeOfObject; /** Current value type of map object */	
	protected Shape shapeObject; /**The reference to the basic shape object (created in derived class constructor)*/
		
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
	
	/**
	 * Simple getter
	 * @return
	 */
	public String getTextureName( )
	{
		return textureName;
	}
	
	/**
	 * Simple setter
	 * @param textureName
	 */
	public void setTextureName( String textureName )
	{
		this.textureName = textureName;
	}
	
	/**
	 * Simple getter
	 * @return
	 */
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
		if( ! isTypeAllowed( type ) )
		{
			throw new InvalidTypeOfMapObjectException();			
		}
		this.typeOfObject = type;
	}	
	
	/**
	 * Resize MapShape to given bounds size
	 * @param w - target width of bounds
	 * @param h - target height of bounds
	 */
	abstract public void resize(int w, int h);
	
	/**
	 * Move MapShape to given bounds coordinations
	 * @param x - target X coordinate
	 * @param y - target Y coordinate
	 */
	abstract public void move(int x, int y);
	
	/**
	 * Check if given TypeOfMapObject is allowed for this particular MapShape
	 * @param type
	 * @return boolean
	 */
	abstract protected boolean isTypeAllowed( TypeOfMapObject type );
}


/**
 * MapPolygon - based on java.awt.polygon as shape value 
 * @author fiedukow
 * @see java.awt.Polygon
 * @see MapShape
 */
final class MapPolygon extends MapShape
{
	
	protected static Vector<TypeOfMapObject> allowedTypes; /** Contains list of types allowed on this particular MapShape derived class. */
	
	/**
	 * List of allowed types
	 */
	static
	{
		allowedTypes = new Vector<TypeOfMapObject>();
		allowedTypes.add( TypeOfMapObject.DESTROY );
		allowedTypes.add( TypeOfMapObject.STOP );
		allowedTypes.add( TypeOfMapObject.BUMP );
	}
	
	
	/**
	 * argument-less constructor (not recommended to be used by end user) 
	 */
	public MapPolygon()
	{
		super("");
		shapeObject = new Polygon();
	}

	/**
	 * Initialize empty polygon with selected texture
	 * @param textureName
	 * @see java.awt.Polygon
	 */	
	public MapPolygon( String textureName )
	{
		super( textureName );
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
			System.err.println("Sponsorem niemozliwego do wysapienia bledu jest programista, ktory najwyrazniej popelnil blad :-(");
		}
	}
	
	/**
	 * java.awt.Polygon object accessor which allow to add point to this Polygon
	 * @param x
	 * @param y
	 * @see java.awt.Polygon#addPoint(int, int)
	 */
	public void addPoint( int x, int y )
	{		
		getPolygon().addPoint(x,y);
	}
	
	/**
	 * Give xpoints form java.awt.Polygon
	 * @return int[]
	 * @see java.awt.Polygon#xpoints
	 */
	public int[] getXCoords()
	{
		return getPolygon().xpoints;
	}
	
	/**
	 * Give ypoints form java.awt.Polygon
	 * @return int[]
	 * @see java.awt.Polygon#ypoints
	 */
	public int[] getYCoords()
	{
		return getPolygon().xpoints;
	}

	
	/**
	 * Resize polygon to the given Bounds size.
	 * @param h - new height of polygon bounds
	 * @param w - new width of polygon bounds
	 * @see java.awt.Polygon#getBounds()
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
	 * @param x new X coordinate for upper left corner of polygon bounds
	 * @param y new Y coordinate for upper left corner of polygon bounds
	 * @see java.awt.Polygon#getBounds()
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
	 * PROTECTED SECTION
	 */
	/**
	 * @see MapShape#isTypeAllowed(TypeOfMapObject) 
	 */
	protected boolean isTypeAllowed( TypeOfMapObject type ){
		return allowedTypes.contains( type );
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
}


/**
 * MapRectangle - based on java.awt.Rectangle as shape value 
 * @author fiedukow
 * @see java.awt.Rectangle
 * @see MapShape
 */
final class MapRectangle extends MapShape
{
	protected static Vector<TypeOfMapObject> allowedTypes; /** Contains list of types allowed on this particular MapShape derived class. */
	
	/**
	 * List of allowed types
	 */
	static
	{
		allowedTypes = new Vector<TypeOfMapObject>();
		allowedTypes.add( TypeOfMapObject.DESTROY );
		allowedTypes.add( TypeOfMapObject.STOP );
		allowedTypes.add( TypeOfMapObject.BUMP );
		allowedTypes.add( TypeOfMapObject.QUAY );
	}
	
	/**
	 * argument-less constructor (not recommended to be used by end user) 
	 */
	public MapRectangle()
	{
		super("");
		shapeObject = new Rectangle();
	}
	
	
	/**
	 * Initialize rectangle with selected texture, position and size
	 * @param textureName
	 */	
	public MapRectangle( String textureName, int x, int y, int w, int h )
	{
		super( textureName );
		this.shapeObject = new Rectangle(x,y,w,h);	
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
	 * Resize rectangle to the given size.
	 * @param h - new height of rectangle
	 * @param w - new width of rectangle
	 * @see java.awt.Rectangle#setSize(int, int)
	 */
	public void resize( int w, int h )
	{
		getRectangle().setSize(w, h);
	}
	
	/**
	 * Move rectangle to place where first (upper left) corner of it is in given place. 
	 * @param x x coordinate for upper left corner of rectangle
	 * @param y y coordinate for upper left corner of rectangle
	 * @see java.awt.Rectangle#setLocation(int, int)
	 */
	public void move( int x, int y )
	{
		getRectangle().setLocation(x, y);
	}
	
	/**
	 * Shape (java.awt.Rectangle) getX accessor
	 * @return int
	 * @see java.awt.Rectangle#getX()
	 */
	public int getX()
	{
		return (int) getRectangle().getX();
	}
	
	/**
	 * Shape (java.awt.Rectangle) getY accessor
	 * @return int
	 * @see java.awt.Rectangle#getX()
	 */
	public int getY()
	{
		return (int) getRectangle().getY();
	}
	
	
	/**
	 * Shape (java.awt.Rectangle) getWidth accessor
	 * @return int
	 * @see java.awt.Rectangle#getWidth()
	 */
	public int getW()
	{
		return (int) getRectangle().getWidth();
	}
	
	/**
	 * Shape (java.awt.Rectangle) getHeight accessor
	 * @return int
	 * @see java.awt.Rectangle#getHeight()
	 */
	public int getH()
	{
		return (int) getRectangle().getHeight();
	}
	
	/**
	 * Shape (java.awt.Rectangle) setRect accessor
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @see java.awt.Rectangle#setRect(double, double, double, double)
	 */
	public void setRectangle( int x, int y, int w, int h )
	{
		getRectangle().setRect(x, y, w, h);
	}
	
	
	/*
	 * PROTECTED SECTION
	 */
	/**
	 * @see MapShape#isTypeAllowed(TypeOfMapObject) 
	 */
	protected boolean isTypeAllowed( TypeOfMapObject type ){
		return allowedTypes.contains( type );
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
}



/**
 * MapEllipse - based on java.awt.Rectangle as shape value 
 * @author fiedukow
 * @see java.awt.geom.Ellipse2D.Double
 * @see MapShape
 */
final class MapEllipse extends MapShape
{
	
	protected static Vector<TypeOfMapObject> allowedTypes; /** Contains list of types allowed on this particular MapShape derived class. */
	
	/**
	 * List of allowed types
	 */
	static
	{		
		allowedTypes = new Vector<TypeOfMapObject>();
		allowedTypes.add( TypeOfMapObject.DESTROY );
		allowedTypes.add( TypeOfMapObject.STOP );
		allowedTypes.add( TypeOfMapObject.BUMP );
	}
	
	/**
	 * argument-less constructor (not recommended to be used by end user) 
	 */
	public MapEllipse()
	{
		super("");
		shapeObject = new Ellipse2D.Double();
	}
	
	/**
	 * Initialize Ellipse with selected texture, position and size
	 * @param textureName
	 * @param x - x possition of bound
	 * @param y - y possition of bound
	 * @param w - width of bound
	 * @param h - height of bound
	 */
	public MapEllipse( String textureName, int x, int y, int w, int h )
	{
		super( textureName );
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
	 * Resize Ellipse to the given bound size.
	 * @param h - new height of ellipse
	 * @param w - new width of ellipse
	 * @see java.awt.geom.Ellipse2D.Double#setFrame(double,double,double,double)
	 */
	public void resize( int w, int h )
	{
		getEllipse().setFrame(getX(), getY(), w, h);
	}
	
	/**
	 * Move ellipse to place where first (upper left) corner of it bound is in given place. 
	 * @param x x coordinate for upper left corner of ellipse bound
	 * @param y y coordinate for upper left corner of ellipse bound
	 * @see java.awt.geom.Ellipse2D.Double#setFrame(double,double,double,double)
	 */
	public void move( int x, int y )
	{
		getEllipse().setFrame(x, y, getW(), getH());
	}
	
	
	/**
	 * Shape (java.awt.geom.Ellipse2D.Double) getX accessor
	 * @return int
	 * @see java.awt.geom.Ellipse2D.Double#getX()
	 */
	public int getX()
	{
		return (int) getEllipse().getX();
	}
	
	/**
	 * Shape (java.awt.geom.Ellipse2D.Double) getY accessor
	 * @return int
	 * @see java.awt.geom.Ellipse2D.Double#getY()
	 */
	public int getY()
	{
		return (int) getEllipse().getY();
	}
	
	/**
	 * Shape (java.awt.geom.Ellipse2D.Double) getWidth accessor
	 * @return int
	 * @see java.awt.geom.Ellipse2D.Double#getWidth()
	 */
	public int getW()
	{
		return (int) getEllipse().getWidth();
	}
	
	/**
	 * Shape (java.awt.geom.Ellipse2D.Double) getHeight accessor
	 * @return int
	 * @see java.awt.geom.Ellipse2D.Double#getHeight()
	 */
	public int getH()
	{
		return (int) getEllipse().getHeight();
	}
	
	/**
	 * Shape (java.awt.geom.Ellipse2D.Double) setFrame accessor
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @see java.awt.geom.Ellipse2D.Double#setFame(int,int,int,int)
	 */
	public void setEllipse( int x, int y, int w, int h )
	{
		getEllipse().setFrame(x, y, w, h);
	}
	
	
	/*
	 * PROTECTED SECTION
	 */
	/**
	 * @see MapShape#isTypeAllowed(TypeOfMapObject) 
	 */
	protected boolean isTypeAllowed( TypeOfMapObject type ){
		return allowedTypes.contains( type );
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
}
