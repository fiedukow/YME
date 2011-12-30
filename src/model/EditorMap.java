package model;
import java.awt.Point;
import java.util.Vector;

/**
 * The map description for map Editor. Have some get/set function as interface.
 * @author fiedukow
 */
public final class EditorMap
{
	private String mapName;					/**Map name, for general proposes*/
	private String waterTexture;		    /**The texture used as the background*/
	private Vector<MapShape> shapes;		/**List of polygons presented on the map*/
	private Point startPoint;
	
	/**
	 * Main constructor of Map
	 * @param mapName
	 * @param waterTexture
	 * @param shapes
	 */
	public EditorMap( String mapName, String waterTexture, Vector<MapShape> shapes, Point startPoint )
	{
		this.mapName = mapName;
		this.waterTexture = waterTexture;
		this.shapes = shapes;
		this.startPoint = startPoint;
	}
	
	/**
	 * @return the startPoint
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * @param startPoint the startPoint to set
	 */
	/*package*/ void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Simple setter
	 * @param mapName
	 */
	/*package*/ void setMapName( String mapName )
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
	/*package*/ void setWaterTexture( String waterTexture )
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
	 * @return the shapes
	 */
	public Vector<MapShape> getShapes() {
		return shapes;
	}

	/**
	 * Adds polygon to the map.
	 * @param poly
	 */
	/*package*/ void addMapShape( MapShape sh )
	{
		getShapes().add( sh );
	}
	
	/**
	 * Removes polygon from map.
	 * @param id index of the one that should be removed
	 */
	/*package*/ void removeMapShape( int id )
	{
		getShapes().remove(id);
	}
	
	/*package*/ void removeMapShape( MapShape shape )
	{
		getShapes().remove( shape );
	}

}



