package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Vector;

import javax.swing.JPanel;

import controller.FocusType;
import controller.ViewState;
import controller.event.EventPointAccept;
import controller.event.EventPointSelect;

import model.MapShape;

/**
 * Provides way to draw a map in human readable form.
 * It also generate some basic Events connected with map.
 * @author fiedukow
 */
public class MapPanel extends JPanel
{
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The view which created this MapPanel
	 */
	private View father; 
	
	/**
	 * Main constructor - creates panel with father setted
	 * Using father you can easily repaint using newest snapshot every time you want
	 * What's more father allow to push events to queue
	 * @param father_ - the view which created this MapPanel
	 */
	public MapPanel( View father_ )
	{
		this.father = father_;		
		
		addMouseListener(
		new MouseAdapter() 
        { 			
            public void mousePressed(final MouseEvent me)
            {            
            	if( me.getClickCount() == 1 )
            		father.pushEvent( new EventPointSelect(me) );
            	if( me.getClickCount() == 2 )
            		father.pushEvent( new EventPointAccept(me) );
            }             
        }); 
	}

	/**
	 * How to repaint this map.
	 * It describes the way to draw all elements on map and  is called automaticly with repaint();
	 */
	public void paintComponent(Graphics g)
	{
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = this.getWidth();     
        int h = this.getHeight();    
        setTexturePaint("textures/"+getState().getMap().getWaterTexture(), g2); //FIXME myGraphics2D        
        g2.fill(new Rectangle(0,0,w,h));                                     

        int i = 0;
        
        try{
        	for( MapShape shape : getState().getMap().getShapes() )
        	{        
        		drawShape(shape, g2, getState().getFocusType() == FocusType.SHAPE && getState().getFocusId() == i);
        		++i;
        	}   
        }
        catch ( ConcurrentModificationException e )
        {
        	/*TODO shouldn't i implement cloneable and give view the copy of map instead of model object*/
        	this.repaint();
        	return;
        }
        
        drawStartPoint( g2 );
        drawBufferedPolygon( g2 );
	}
	
	/**
	 * It draws not completed polygon started by user as a number of thick lines.
	 * @param g2 - Graphics2D object to draw on.
	 */
	private void drawBufferedPolygon( Graphics2D g2 )
	{
		Vector<Point> bufferedVerticles = getState().getPolygonBuffer().getVerticles();
    
	    if( bufferedVerticles.size() > 0 )
	    {
	    	g2.setPaint(Color.RED);
	        if( bufferedVerticles.size() == 1 )
	        {
	        	Point p = bufferedVerticles.get(0);
	        	g2.drawOval( (int) p.getX()-2, (int) p.getY()-2, 5, 5 );
	        	g2.fillOval( (int) p.getX()-2, (int) p.getY()-2, 5, 5 );
	        }
	        else
	        {
	        	for( int i = 0; i < bufferedVerticles.size()-1 ; ++i )
	        	{        		
	        		drawThickLine( 
	        				 	g2, 
	        				 	(int) bufferedVerticles.get(i).getX(), 
	        				 	(int) bufferedVerticles.get(i).getY(),        					 
	        				 	(int) bufferedVerticles.get(i+1).getX(), 
	        				 	(int) bufferedVerticles.get(i+1).getY(),
	        				 	3,
	        				 	Color.RED
	        					);
	        	}
	        }
	    }
	}
	
	/**
	 * Draw MapShape on MapPanel - if it is focus - use yellow border 
	 * @param shape - MapShape to draw
	 * @param g2 - Graphics2D object to draw on. 
	 * @param focus - true if this shape is focused
	 */
	void drawShape( MapShape shape, Graphics2D g2, boolean focus )
	{
		setTexturePaint("textures/"+shape.getTextureName(), g2);
		g2.fill(shape.getShapeObject());
		if( ! focus )
			g2.setPaint(Color.BLACK);
		else
			g2.setPaint(Color.YELLOW);
		
		g2.draw(shape.getShapeObject());
	}		
	
	/**
	 * Draw symbol of start point where it should be.
	 * @param g2 - Graphics2D object to draw on.
	 */
	private void drawStartPoint( Graphics2D g2 )
	{
		Point startPoint = getState().getMap().getStartPoint();
	    int range = getState().getStartPointRange();
	    setTexturePaint("textures/start.jpg", g2); //FIXME myGraphics2D 
      
	    Ellipse2D.Double startPointShape =  
	    		new Ellipse2D.Double
	    			(
	    				(int)startPoint.getX()-range,
	        			(int)startPoint.getY()-range, 
	        			range*2,
	        			range*2
	        		);
	        
	    g2.fill(startPointShape);        
	    g2.setPaint(Color.YELLOW);
	        
	    if( getState().getFocusType() == FocusType.START_POINT )
	    	g2.setPaint(Color.YELLOW);
	    else
	    	g2.setPaint(Color.BLACK);
	        
	    g2.draw(startPointShape);        
	}
	
	/**
	 * Sets paint on brush to texture given by parameter using TextureCache
	 * @param fileName - path to texture (jpg)
	 * @param g2 - Grapics2D object with brush to be changed
	 */
    static private void setTexturePaint(String fileName, Graphics2D g2)
    {
        BufferedImage img;
        try
        {
            img = TextureCache.getInstance().get( fileName );
        }
        catch(IOException e)
        {        
            System.err.println("Blad odczytu tekstury "+fileName);
            g2.setPaint(Color.RED);
            return;
        }
        if( img == null ) return;

        Rectangle textureAnchor = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        TexturePaint paint = new TexturePaint(img, textureAnchor);
        g2.setPaint(paint);
    }
    
    /**
     * Accessor to father method
     * @return current state of View (snapshot)
     */
    private ViewState getState()
    {
    	return father.getState();
    }
    
    
	/**
	 * Draws thick lane using polygons.
	 * @author Real Gagnon (rgagnon.com)
	 * @param g
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param thickness
	 * @param c
	 */
	private void drawThickLine( Graphics g, int x1, int y1, int x2, int y2, int thickness, Color c ) 
	{
		// The thick line is in fact a filled polygon
		g.setColor(c);
		int dX = x2 - x1;
		int dY = y2 - y1;
		// line length
		double lineLength = Math.sqrt(dX * dX + dY * dY);

		double scale = (double)(thickness) / (2 * lineLength);

		// The x,y increments from an endpoint needed to create a rectangle...
		double ddx = -scale * (double)dY;
		double ddy = scale * (double)dX;
		ddx += (ddx > 0) ? 0.5 : -0.5;
		ddy += (ddy > 0) ? 0.5 : -0.5;
		int dx = (int)ddx;
		int dy = (int)ddy;

		// Now we can compute the corner points...
		int xPoints[] = new int[4];
		int yPoints[] = new int[4];

		xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
		xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
		xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
		xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

		g.fillPolygon(xPoints, yPoints, 4);
	}
}
