package model;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

/**
 * Universal YahtMapFormat container. 
 * Have only objects with attributes saved in XML file. 
 * Can communicate with XML to initialize (but cannot be overloaded! 
 * You need to create new object instead) or to save state.
 */
final class MapTranslator
{
	private final String mapName;					/** Map name, for general proposes */
	private final String waterTexture;				/** The texture used as the background */
	private final Point  startPoint;
	private final ArrayList<XMLShape> shapes;	    /** List of polygons presented on the map */

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
		applyAliases(xstream);
		File file = new File(fileName);
		if( !file.isFile() ) throw new FileNotFoundException();
		tmp = (MapTranslator) xstream.fromXML( file ); /*rewrite whole object*/
		this.mapName = tmp.mapName;
		this.waterTexture = tmp.waterTexture;
		this.shapes = tmp.shapes;
		this.startPoint = tmp.startPoint;
	}	

	/**
	 * Use EditorMap object to create current map state
	 * @param Map - map to create MapTranslator object
	 */
	public MapTranslator( EditorMap map )
	{	
		this.mapName = map.getMapName();
		this.waterTexture = map.getWaterTexture();
		this.startPoint = map.getStartPoint();
		this.shapes = new ArrayList<XMLShape>();
		
		for( MapShape sh : map.getShapes() )
		{	
			try{
				this.shapes.add(XMLShape.create(sh));
			}
			catch ( UnrecognizedTypeOfMapShape e )
			{
				System.err.println("Nierozpoznany typ przy produkcji typow XMLShape");
			}
		}
	}
	
	/**
	 * Translate itself to the EditorMap format
	 * @return Generated EditorMap - main model Object
	 */
	public EditorMap translate()
	{		
		ArrayList<MapShape> resultShapes = new ArrayList<MapShape>();		
		for( XMLShape sh : shapes )
		{
			resultShapes.add( sh.translate() );
		}
		EditorMap result = new EditorMap( mapName, waterTexture, resultShapes, startPoint );		
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
		applyAliases(xstream);
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
	
	/**
	 * Created to hide all aliases in one place
	 * @param xstream - stream which want to know MapTranslator aliases 
	 */
	static private void applyAliases( XStream xstream ){
		xstream.alias("map", MapTranslator.class);
		xstream.alias("polygon", XMLPolygon.class);		
		xstream.alias("rectangle", XMLRectangle.class);
		xstream.alias("ellipse", XMLEllipse.class);
		xstream.alias("point", java.awt.Point.class);
	}
}
