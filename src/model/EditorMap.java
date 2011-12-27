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



