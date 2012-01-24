package controller.event;

public class EventLoadMap extends Event 
{
	private final String filePath;
	
	public EventLoadMap( String filePath )
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
		return EventType.LOAD_MAP;
	}	
}
