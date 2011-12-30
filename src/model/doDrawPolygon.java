package model;

import java.awt.Point;
import java.util.Vector;

/**
 * 
 * @author fiedukow
 */
public class doDrawPolygon implements Command
{	
	MapPolygon polygon;
	public doDrawPolygon( String textureName, Vector<Point> points, TypeOfMapObject type )
	{
		polygon = new MapPolygon( textureName, type);
		for( Point point : points )
			polygon.addPoint((int)point.getX(), (int)point.getY());			
	}
	
	public void invoke( EditorMap map ) throws CommandInvokeException
	{
		map.addMapShape( polygon );
	} 
	public void undo( EditorMap map ) throws CommandUndoException
	{
		map.removeMapShape( polygon );
	}
}