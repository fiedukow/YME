package model;

import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 * @author fiedukow
 */
public class doDrawPolygon implements Command
{	
	MapPolygon polygon;
	public doDrawPolygon( String textureName, ArrayList<Point> arrayList, TypeOfMapObject type )
	{
		polygon = new MapPolygon( textureName, type);
		for( Point point : arrayList )
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