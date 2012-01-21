package view; 

import java.util.HashMap;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;


/**
 * Image Buffer singleton which allows to keep textures in RAM
 * @author fiedukow
 */
public class TextureCache
{
	/**
	 * map with textures already buffered
	 */
    private final HashMap<String, BufferedImage> textures;
    
    /**
     * the only instance of this class
     */
    static private final TextureCache instance = new TextureCache();
        
    /**
     * private constructor - creates hash map
     */
    private TextureCache(){
        textures = new HashMap<String, BufferedImage>();
    }
    
    /**
     * Allow to use the only instance of this class
     * @return the only instance
     */
    public static TextureCache getInstance()
    {
    	return instance;
    }
    
    /**
     * Gets texture from cache - reads it from HDD if needed
     * @param textureName - path to texture (jpg) which should be returned
     * @return buffered image ready to use eg. as paint
     * @throws IOException - if file isn't proper
     */
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
