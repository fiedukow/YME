package controller;

import java.util.HashMap;
import java.util.Vector;

import model.EditorMap;
import model.EditorToolbox;

public class ViewState 
{
	EditorMap map;
	EditorToolbox box;
	FocusType focusType;
	Integer focusId;
	Tool selectedTool;		
	PolygonBuffer polygonBuffer; //TODO it should have universal Shape OtherThings in here
	static HashMap< FocusType, Vector<ViewQuestion> > typesQuestions;
	
	static
	{
		typesQuestions = new HashMap< FocusType, Vector<ViewQuestion> >();					
		typesQuestions.put( FocusType.MAP, new Vector<ViewQuestion>());
		typesQuestions.put( FocusType.START_POINT, new Vector<ViewQuestion>());
		typesQuestions.put( FocusType.SHAPE, new Vector<ViewQuestion>());
		
		typesQuestions.get(FocusType.MAP).add(new ViewQuestion("Nazwa", QuestionType.STRING));
		typesQuestions.get(FocusType.MAP).add(new ViewQuestion("Tekstura", QuestionType.FILE_CHOOSE));
		
		typesQuestions.get(FocusType.START_POINT).add(new ViewQuestion("Pozycja", QuestionType.TWICE_INT));
		
		typesQuestions.get(FocusType.SHAPE).add(new ViewQuestion("Tekstura", QuestionType.FILE_CHOOSE));
		typesQuestions.get(FocusType.SHAPE).add(new ViewQuestion("Pozycja", QuestionType.TWICE_INT));
		typesQuestions.get(FocusType.SHAPE).add(new ViewQuestion("Rozmiar", QuestionType.TWICE_INT));		
	}
	
	public ViewState( EditorMap map, EditorToolbox box )
	{
		this.map = map;
		this.box = box;
		focusType = FocusType.MAP;
		selectedTool = Tool.SELECTOR;
		polygonBuffer = new PolygonBuffer();
		focusId = null;		
	}

	/**
	 * @return the polygonBuffer
	 */
	public PolygonBuffer getPolygonBuffer() {
		return polygonBuffer;
	}

	/**
	 * @param polygonBuffer the polygonBuffer to set
	 */
	/*package*/ void setPolygonBuffer(PolygonBuffer polygonBuffer) {
		this.polygonBuffer = polygonBuffer;
	}

	/**
	 * @return the map
	 */
	public EditorMap getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	void setMap(EditorMap map) {
		this.map = map;
	}
	
	/**
	 * @return the focusType
	 */
	public FocusType getFocusType() {
		return focusType;
	}

	/**
	 * @param focusType the focusType to set
	 */
	void setFocusType(FocusType focusType) {
		this.focusType = focusType;
	}

	/**
	 * @return the selectedTool
	 */
	public Tool getSelectedTool() {
		return selectedTool;
	}
	
	
	public boolean isUndoNotEmpty()
	{
		return box.getUndoSize()>0;
	}
	
	public boolean isRedoNotEmpty()
	{
		return box.getRedoSize()>0;
	}
	

	/**
	 * @param selectedTool the selectedTool to set
	 */
	void setSelectedTool(Tool selectedTool) {
		this.selectedTool = selectedTool;
	}

	/**
	 * @return the focusId
	 */
	public Integer getFocusId() {
		return focusId;
	}

	/**
	 * @param focusId the focusId to set
	 */
	void setFocusId(Integer focusId) {
		this.focusId = focusId;
	}
	
	void setFocus( FocusType focusType, Integer focusId )
	{
		setFocusType(focusType);
		setFocusId(focusId);
	}
	
	void setFocus( FocusType focusType )
	{
		setFocusType( focusType );
		setFocusId( -1 );
	}
	
	/**
	 * @return the box
	 */
	public EditorToolbox getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	void setBox(EditorToolbox box) {
		this.box = box;
	}

	public int getStartPointRange()
	{
		return 25;
	}

	
	public Vector<ViewQuestion> getQuestions()
	{
		return typesQuestions.get(focusType);
	}
	
}