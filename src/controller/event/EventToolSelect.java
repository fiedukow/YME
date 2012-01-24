package controller.event;

import controller.Tool;

public class EventToolSelect extends Event 
{
	private final Tool tool;

	public EventToolSelect( Tool tool )
	{
		this.tool = tool;
	}
		
	/**
	 * @return the tool
	 */
	public Tool getTool() {
		return tool;
	}

	public EventType getEventType() {
		return EventType.TOOL_SELECT;
	}	
}
