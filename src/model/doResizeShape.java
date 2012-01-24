package model;


public class doResizeShape implements Command
{
	private MapShape shape;
	private final int w,h,whichElement;
	
	public doResizeShape( int w, int h, int whichElement )
	{
		this.w = w;
		this.h = h;		
		this.whichElement = whichElement;
	}	
		
	public void invoke(EditorMap map) throws CommandInvokeException {
		MapShape toChange = map.getShape( whichElement );		
		shape = toChange.clone();	
		toChange.resize(w, h);		
	}

	public void undo(EditorMap map) throws CommandUndoException {
		map.removeMapShape( whichElement );		
		map.addMapShape( whichElement, shape );
	}
}
