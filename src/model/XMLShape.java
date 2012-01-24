package model;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Base class for all XML represented shapes allowed in program
 * @author fiedukow
 */
abstract class XMLShape
{	
	private final String textureName;	
	private final TypeOfMapObject typeOfObject; /** Current value type of map object */	
	/**
	 * Main constructor - do default action for all derived classes
	 * @param shape - MapShape with informations to convert into XML format
	 */
	public XMLShape ( MapShape shape )
	{
		this.textureName = shape.getTextureName();
		this.typeOfObject = shape.getTypeOfObject();
	}
	
	/**
	 * Factory of XML objects. For now its really ugly, but works. It should be rewritten in future.
	 * @param shape - MapShape used to recognize type of XMLShape, which should be converted into XML format.
	 * @return - new object with XML representation of given shape
	 * @throws UnrecognizedTypeOfMapShape - when the derived type of MapShape isn't known by XMLShape
	 */
	public static XMLShape create( MapShape shape ) throws UnrecognizedTypeOfMapShape
	{
		if( shape instanceof MapPolygon )
			return new XMLPolygon( (MapPolygon) shape);
		if( shape instanceof MapRectangle )
			return new XMLRectangle( (MapRectangle) shape);
		if( shape instanceof MapEllipse )
			return new XMLEllipse( (MapEllipse) shape);					
		throw new UnrecognizedTypeOfMapShape();
	}
	
	/**
	 * Method which make the part of translation which is same for every XMLShape to MapShape conversion.
	 * This method CREATE object using argument-less construction.
	 * @param classToBeCreated class object of item that should be created
	 * @return MapShape with textureName and typeOfMapObject set.
	 */
	public MapShape translate( Class< ? extends MapShape> classToBeCreated )
	{		
		MapShape result = null;

		try {
			result = classToBeCreated.newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e1) 
		{		
			System.err.println("Nie udalo sie stworzyc obiektu.");
			throw new RuntimeException();
		}
		
		result.setTextureName(textureName);
		
		try
		{
			result.setTypeOfObject(typeOfObject);
		}
		catch ( InvalidTypeOfMapObjectException e )
		{
			System.err.println("Translacja ksztaltu o przypisanym niedozwolonym ksztalcie");
		}
		
		return result;
	}
	
	/**
	 * Used to convert this XMLShape object into MapShape object. It probably should call super.translate(XMLClassName.class)
	 * at the beginning. 
	 * @return MapShape object ready to use.
	 */
	abstract MapShape translate();
	
}

/**
 * XMLPolygon ready to serialize using XStream. Only for save/load proposes. 
 * @author fiedukow
 */
class XMLPolygon extends XMLShape
{ 	
	private final ArrayList<Point> vertices;
	
	/**
	 * Main constructor - creates XMLPolygon using MapPolygon
	 * @param polygon - 
	 */
	public XMLPolygon( MapPolygon polygon )
	{		
		super(polygon);
		vertices = new ArrayList<Point>();		
		for( int i = 0; i < polygon.getNPoints(); ++i )
		{			
			vertices.add( new Point(polygon.getXCoords()[i], polygon.getYCoords()[i]) );
		}
	}
	
	/**
	 * Convert this XMLShape object into MapShape element
	 * @return MapPolygon - ready to use MapShape element
	 */
	public MapPolygon translate()
	{
		MapPolygon result = (MapPolygon) translate(MapPolygon.class);		
		for( Point pt : this.vertices )
		{
			result.addPoint((int)pt.getX(), (int)pt.getY());
		}
		return result;
	}	
}


/**
 * XMLRectangle ready to serialize using XStream. Only for save/load proposes. 
 * @author fiedukow
 */
class XMLRectangle extends XMLShape
{ 	
	private final int x,y,w,h;
	
	/**
	 * Main constructor - creates XMLRectagle using MapRectangle
	 * @param rectangle
	 */
	public XMLRectangle( MapRectangle rectangle )
	{		
		super(rectangle);
		x = rectangle.getX();
		y = rectangle.getY();
		w = rectangle.getW();
		h = rectangle.getH();		
	}
	
	/**
	 * Convert this XMLShape object into MapShape element
	 * @return MapRectangle - ready to use MapShape element
	 */
	public MapRectangle translate()
	{
		MapRectangle result = (MapRectangle) translate(MapRectangle.class);		
		result.setRectangle(x, y, w, h);
		return result;
	}	
}


/**
 * XMLEllipse ready to serialize using XStream. Only for save/load proposes. 
 * @author fiedukow
 */
class XMLEllipse extends XMLShape
{ 	
	private final int x,y,w,h;
	
	/**
	 * Main constructor - creates XMLElllipse using MapEllipse
	 * @param ellipse
	 */
	public XMLEllipse( MapEllipse  ellipse )
	{		
		super(ellipse);
		x = ellipse.getX();
		y = ellipse.getY();
		w = ellipse.getW();
		h = ellipse.getH();		
	}
	
	/**
	 * Convert this XMLShape object into MapShape element
	 * @return MapEllipse - ready to use MapShape element
	 */
	public MapEllipse translate()
	{
		MapEllipse result = (MapEllipse) translate(MapEllipse.class);		
		result.setEllipse(x, y, w, h);
		return result;
	}	
}

/**
 * Exception object to use when XMLShape factory didn't recognize derived MapShape type 
 * @author fiedukow
 *
 */
class UnrecognizedTypeOfMapShape extends Exception
{

	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
}