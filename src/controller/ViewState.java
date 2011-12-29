package controller;

import model.EditorMap;

public class ViewState 
{
	EditorMap map;
	FocusType focusType;
	Integer focusId;
	Tool selectedTool;	
	
	public ViewState( EditorMap map )
	{
		this.map = map;
		focusType = FocusType.MAP;
		selectedTool = Tool.SELECTOR;
		focusId = null;		
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
	

	
}