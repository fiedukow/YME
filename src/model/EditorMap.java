package model;
import java.awt.Point;
import java.util.ArrayList;

import controller.question.DoubleIntValueQuestion;
import controller.question.QuestionType;
import controller.question.StringValueQuestion;
import controller.question.ViewQuestion;
import controller.question.WrongQuestionTypeException;

/**
 * The map description for map Editor. Have some get/set function as interface.
 * @author fiedukow
 */
public final class EditorMap implements Cloneable
{
	private String mapName;					/**Map name, for general proposes*/
	private String waterTexture;		    /**The texture used as the background*/
	private ArrayList<MapShape> shapes;		/**List of polygons presented on the map*/
	private Point startPoint;
	
	/**
	 * Main constructor of Map
	 * @param mapName
	 * @param waterTexture
	 * @param resultShapes
	 */
	public EditorMap( String mapName, String waterTexture, ArrayList<MapShape> resultShapes, Point startPoint )
	{
		this.mapName = mapName;
		this.waterTexture = waterTexture;
		this.shapes = resultShapes;
		this.startPoint = startPoint;
	}
	
	public EditorMap clone()
	{
		ArrayList<MapShape> resultArray = new ArrayList<MapShape>();
		for( MapShape shape : shapes )
		{
			resultArray.add( shape.clone() );
		}
		return new EditorMap( new String( mapName ), new String ( waterTexture ), resultArray, new Point( startPoint ) );
	}
	
	/**
	 * @return the startPoint
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * @param startPoint the startPoint to set
	 */
	/*package*/ void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Simple setter
	 * @param mapName
	 */
	/*package*/ void setMapName( String mapName )
	{
		this.mapName = mapName;
	}
	
	/**
	 * Simple getter
	 * @return
	 */
	public String getMapName( )
	{
		return this.mapName;
	}
	
	/**
	 * Simple setter
	 * @param waterTexture
	 */
	/*package*/ void setWaterTexture( String waterTexture )
	{
	    this.waterTexture = waterTexture;
	}

	/**
	 * Simple getter
	 * @return 
	 */
	public String getWaterTexture( )
	{
	    return this.waterTexture;
	}
	
	/**
	 * @return the shapes
	 */
	public ArrayList<MapShape> getShapes() {
		return shapes;
	}
	
	/**
	 * Asks question about pointed shape
	 * @param index - id of MapShape to be asked for
	 * @return - collection of questions ready to use
	 */
	public ArrayList< ViewQuestion > getShapeQuestions( int index )
	{
		return getShape(index).getQuestions();
	}
	
	public ArrayList< ViewQuestion > getMapQuestions( )
	{
		 ArrayList< ViewQuestion > questions = new  ArrayList< ViewQuestion >();
		 try {
			questions.add( new StringValueQuestion("mapName",QuestionType.STRING, getMapName() ) );
			questions.add( new StringValueQuestion("texture",QuestionType.STRING, getWaterTexture() ) );
		 } 
		 catch (WrongQuestionTypeException e) 
		 {
			System.err.println("Niepoprawny typ pytania.");
			throw new RuntimeException();
		 }	
		 return questions;
	}
	
	public ArrayList< ViewQuestion > getStartPointQuestions( )
	{
		ArrayList< ViewQuestion > questions = new  ArrayList< ViewQuestion >();
		Point startPoint = this.getStartPoint();				
		try {
			questions.add( new DoubleIntValueQuestion("position",QuestionType.TWICE_INT, (int) startPoint.getX(), (int) startPoint.getY() ) );
		} catch (WrongQuestionTypeException e) {
			System.err.println("Niepoprawny typ pytania.");
			throw new RuntimeException();
		}	
		return questions;
	}
	

	/**
	 * Shape under index
	 * @param index - which element you want to choose
	 * @return
	 */
	MapShape getShape( int index )
	{
		return shapes.get(index);
	}

	/**
	 * Adds polygon to the map.
	 * @param poly
	 */
	/*package*/ void addMapShape( MapShape sh )
	{
		getShapes().add( sh );
	}
	
	/**
	 * Adds polygon to the map on selected position.
	 * @param poly
	 */
	/*package*/ void addMapShape( int index, MapShape sh )
	{
		getShapes().add( index, sh );
	}
	
	/**
	 * Removes polygon from map.
	 * @param id index of the one that should be removed
	 */
	/*package*/ void removeMapShape( int id )
	{
		getShapes().remove( id );
	}
	
	/*package*/ void removeMapShape( MapShape shape )
	{
		getShapes().remove( shape );
	}
		
}



