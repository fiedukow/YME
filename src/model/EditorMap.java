package model;
import java.util.Vector;

/**
 * The map description for map Editor. Have some get/set function as interface.
 * @author fiedukow
 */
final class EditorMap
{
	private String mapName;					/**Map name, for general proposes*/
	private String waterTexture;		    /**The texture used as the background*/
	private Vector<MapShape> shapes;		/**List of polygons presented on the map*/
	
	/**
	 * Main constructor of Map
	 * @param mapName
	 * @param waterTexture
	 * @param shapes
	 */
	public EditorMap( String mapName, String waterTexture, Vector<MapShape> shapes )
	{
		this.mapName = mapName;
		this.waterTexture = waterTexture;
		this.shapes = shapes;
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
	 * @return the shapes
	 */
	public Vector<MapShape> getShapes() {
		return shapes;
	}

	/**
	 * Adds polygon to the map.
	 * @param poly
	 */
	public void addMapShape( MapShape sh )
	{
		getShapes().add( sh );
	}
	
	/**
	 * Removes polygon from map.
	 * @param id index of the one that should be removed
	 */
	public void removeMapShape( int id )
	{
		getShapes().remove(id);
	}
	
	public void removeMapShape( MapShape shape )
	{
		getShapes().remove( shape );
	}

}



