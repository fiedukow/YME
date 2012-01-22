package model;

public class doResizeShape implements Command
{
	/*TODO, it's generally bad when resize to value close to zero*/
	MapShape shape;
	int w,h,whichElement;
	
	public doResizeShape( int w, int h, int whichElement )
	{
		this.w = w;
		this.h = h;		
		this.whichElement = whichElement;
	}	
		
	public void invoke(EditorMap map) throws CommandInvokeException {
		MapShape shape = map.getShapes().get(whichElement);
		int oldPosition[] = shape.getSize();		
		shape.resize(w, h);
		w = oldPosition[0];
		h = oldPosition[1];
	}

	public void undo(EditorMap map) throws CommandUndoException {
		try {
			invoke( map );
		} catch (CommandInvokeException e) {
			System.err.println("BLAD: Cofanie nie powiodlo sie w wyniku nieznanego bledu!");
			throw new RuntimeException();
		}
	}
}
