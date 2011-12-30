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
		System.out.println("ALE WYWALAM!");
		verticles.clear();
	}
	
	Vector<Point> getVerticles() throws ToFewVerticlesException
	{
		if( verticles.size() >= 3 )
		{
			return verticles;
		}
		throw new ToFewVerticlesException(); 
	}
	
}

class ToFewVerticlesException extends Exception
{
	
}