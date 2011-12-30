package controller.event;

public class EventSaveMap extends Event 
{
	String filePath;
	
	public EventSaveMap( String filePath )
	{		
		this.filePath = filePath;
	}
	
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	
	public EventType getEventType() {
		return EventType.SAVE_MAP;
	}	
}
