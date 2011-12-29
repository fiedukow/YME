package controller.event;

import java.awt.event.MouseEvent;

public class EventPointSelect extends Event 
{
	int x, y;
	
	public EventPointSelect( MouseEvent event )
	{
		this.x = event.getX();
		this.y = event.getY();
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	public EventType getEventType() {
		return EventType.MAP_POINT_SELECT;
	}	
}
