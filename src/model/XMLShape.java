package model;

import java.awt.Point;
import java.util.Vector;

class XMLShape
{
	private String textureName;	
	private TypeOfMapObject typeOfObject; /** Current value type of map object */
	/*TODO universal attribute collection?*/
	
	
	public XMLShape ( MapShape shape )
	{
		this.textureName = shape.getTextureName();
		this.typeOfObject = shape.getTypeOfObject();
	}
	
	/**
	 * Main method for translation between XMLShape and MapShape. Should be used AFTER resultObject initialization
	 * @param resultObject already created object which need to be translated into shape
	 * @return MapShape with textureName and typeOfMapObject set.
	 * @throws  
	 */
	public MapShape translate( Class< ? extends MapShape> classToBeCreated )
	{		
		MapShape result = null;
		try
		{
			result = classToBeCreated.newInstance();
		}
		catch ( Exception e )
		{
			System.err.println("Nie udalo sie stworzyc obiektu.");
			return result;
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
	
}

/**
 * XMLPolygon ready to serialize using XStream. Only for save/load proposes. 
 * @author fiedukow
 */
class XMLPolygon extends XMLShape
{ 	
	/**
	 * Main constructor - creates XMLPolygon using MapPolygon
	 * @param polygon
	 */
	Vector<Point> vertices;
	
	public XMLPolygon( MapPolygon polygon )
	{		
		super(polygon);
		vertices = new Vector<Point>();		
		for( int i = 0; i < polygon.getXCoords().length; ++i )
		{
			vertices.add( new Point(polygon.getXCoords()[i], polygon.getYCoords()[i]) );
		}
	}
	
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


/*TODO Javadoc for the class below */
/**
 * XMLRectangle ready to serialize using XStream. Only for save/load proposes. 
 * @author fiedukow
 */
class XMLRectangle extends XMLShape
{ 	
	/**
	 * Main constructor - creates XMLPolygon using MapPolygon
	 * @param polygon
	 */
	int x,y,w,h;
	
	public XMLRectangle( MapRectangle rectangle )
	{		
		super(rectangle);
		x = rectangle.getX();
		y = rectangle.getY();
		w = rectangle.getW();
		h = rectangle.getH();		
	}
	
	public MapRectangle translate()
	{
		MapRectangle result = (MapRectangle) translate(MapRectangle.class);		
		result.setRectangle(x, y, w, h);
		return result;
	}	
}


/*TODO Javadoc for the class below */
/**
 * XMLRectangle ready to serialize using XStream. Only for save/load proposes. 
 * @author fiedukow
 */
class XMLEllipse extends XMLShape
{ 	
	/**
	 * Main constructor - creates XMLPolygon using MapPolygon
	 * @param polygon
	 */
	int x,y,w,h;
	
	public XMLEllipse( MapEllipse  ellipse )
	{		
		super(ellipse);
		x = ellipse.getX();
		y = ellipse.getY();
		w = ellipse.getW();
		h = ellipse.getH();		
	}
	
	public MapEllipse translate()
	{
		MapEllipse result = (MapEllipse) translate(MapEllipse.class);		
		result.setEllipse(x, y, w, h);
		return result;
	}	
}