package controller.event;

import controller.FocusType;

public class EventChangeFocus extends Event 
{
	Integer focusId;
	FocusType focusType;
	
	public EventChangeFocus( FocusType focusType, Integer focusId )
	{
		this.focusId = focusId;
		this.focusType = focusType;
	}
	
	/**
	 * @return the focusType
	 */
	public FocusType getFocusType() {
		return focusType;
	}

	/**
	 * @return the focusId
	 */
	public Integer getFocusId() {
		return focusId;
	}

	public EventType getEventType() {
		return EventType.CHANGE_FOCUS;
	}	
	
}
