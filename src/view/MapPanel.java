package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JPanel;

import controller.FocusType;
import controller.ViewState;
import controller.event.EventPointAccept;
import controller.event.EventPointSelect;

import model.EditorMap;
import model.MapShape;

public class MapPanel extends JPanel
{
	private View father; 
	
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


	public void paintComponent(Graphics g)
	{
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = this.getWidth();     
        int h = this.getHeight();    
        setTexturePaint("textures/"+getState().getMap().getWaterTexture(), g2); //FIXME myGraphics2D        
        g2.fill(new Rectangle(0,0,w,h));                                     

        int i = 0;
        
        for( MapShape shape : getState().getMap().getShapes() )
        {        
        	drawShape(shape, g2, getState().getFocusType() == FocusType.SHAPE && getState().getFocusId() == i);
        	++i;
        }       
        
        drawStartPoint( g2 );
        drawBufferedPolygon( g2 );
	}
	
	void drawBufferedPolygon( Graphics2D g2 )
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
	
	void drawShape( MapShape sh, Graphics2D g2, boolean focus )
	{
		setTexturePaint("textures/"+sh.getTextureName(), g2);
		g2.fill(sh.getShapeObject());
		if( ! focus )
			g2.setPaint(Color.BLACK);
		else
			g2.setPaint(Color.YELLOW);
		
		g2.draw(sh.getShapeObject());
	}		
	
	void drawStartPoint( Graphics2D g2 )
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
	
	
    private void setTexturePaint(String fileName, Graphics2D g2)
    {
        BufferedImage img;
        try
        {
            img = TextureCache.getInstance().get( fileName );
        }
        catch(IOException e)
        {
        	
            System.err.println("Blad odczytu tekstury "+fileName);
            return;
        }
        if( img == null ) return;

        Rectangle textureAnchor = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        TexturePaint paint = new TexturePaint(img, textureAnchor);
        g2.setPaint(paint);
    }
    
    private ViewState getState()
    {
    	return father.getState();
    }
    
    
	/**
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
