package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import controller.FocusType;
import controller.ViewState;
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
            	father.pushEvent( new EventPointSelect(me) );
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
}
