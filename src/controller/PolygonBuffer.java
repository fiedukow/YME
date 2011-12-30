package controller;

import java.awt.Point;
import java.util.Vector;

public class PolygonBuffer 
{
	Vector<Point> verticles;
	PolygonBuffer()
	{
		verticles = new Vector<Point>();
	}
	
	void addPoint( Point point )	
	{
		verticles.add(point);
	}
	
	void reset()
	{
		verticles.clear();
	}
	
	Vector<Point> getPolygonVerticles() throws ToFewVerticlesException
	{
		if( verticles.size() >= 3 )
		{
			return verticles;
		}
		throw new ToFewVerticlesException(); 
	}
	
	public Vector<Point> getVerticles()
	{	
		return verticles;
	}
	
	
}

class ToFewVerticlesException extends Exception
{
	
}