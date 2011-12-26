package model;

import java.io.IOException;

/*
 * Model class in MVC meaning...
 */
public final class Model 
{
	EditorMap map;
	EditorToolbox box;
	public Model()
	{		
		try
		{
			MapTranslator mapa = new MapTranslator();
			mapa.save("maps/sample.xml");
			mapa = new MapTranslator(mapa.translate());
			mapa.save("maps/sample1.xml");
		}
		catch ( IOException e )
		{
			System.err.println("BLAD ZAPISU!");
		}
		System.out.println("Model created!");
	}
	/*FIXME - i need some interface ;( */
}