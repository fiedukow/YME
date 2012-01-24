package controller;

import java.awt.Point;
import java.util.ArrayList;

public class PolygonBuffer  
{
	private final ArrayList<Point> verticles;
	PolygonBuffer()
	{
		verticles = new ArrayList<Point>();
	}
	
	void addPoint( Point point )	
	{
		verticles.add(point);
	}
	
	void reset()
	{
		verticles.clear();
	}
	
	ArrayList<Point> getPolygonVerticles() throws ToFewVerticlesException
	{
		if( verticles.size() >= 3 )
		{
			return verticles;
		}
		throw new ToFewVerticlesException(); 
	}
	
	public ArrayList<Point> getVerticles()
	{	
		return verticles;
	}
	
	
}

class ToFewVerticlesException extends Exception
{

	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
}