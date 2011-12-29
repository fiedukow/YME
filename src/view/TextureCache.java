package view; 

import java.util.HashMap;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;


/**
 * Image Buffer with allows to save textures in RAM
 * @author fiedukow
 */
public class TextureCache
{
    private HashMap<String, BufferedImage> textures;
    static private TextureCache instance = new TextureCache();
    
    
    private TextureCache(){
        textures = new HashMap<String, BufferedImage>();
    }
    
    public static TextureCache getInstance()
    {
    	return instance;
    }
    
    public BufferedImage get( String textureName ) throws IOException
    {
        BufferedImage img;
        img = textures.get( textureName );
        
        if( img != null ) return img;
        else
        {
            InputStream in = new FileInputStream(textureName); 
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in); //FIXME recognize filetype (jpg,gif,png,bmp - at least)
            img = decoder.decodeAsBufferedImage();
            in.close();
            textures.put( textureName, img );
            return img;
        }
    }
}
