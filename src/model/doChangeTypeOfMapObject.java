package model;


public class doChangeTypeOfMapObject implements Command
{
	private final int whichElement;
	private TypeOfMapObject type;
	
	public doChangeTypeOfMapObject( TypeOfMapObject type, int whichElement )
	{
		this.type = type; 
		this.whichElement = whichElement;
	}
	public void invoke( EditorMap map ) throws CommandInvokeException
	{		
		try {
			MapShape shape = map.getShape( whichElement );
			TypeOfMapObject oldType = shape.getTypeOfObject();
			shape.setTypeOfObject( type );
			type = oldType;
		} catch (InvalidTypeOfMapObjectException e) {
			System.err.println("Proba ustawienia niedozowlonego typu obiektu mapy.");
			throw new RuntimeException();
		}		
	} 
	public void undo( EditorMap map ) throws CommandUndoException
	{
		try {
			invoke(map);
		} catch (CommandInvokeException e) {
			e.printStackTrace();
		}
	}	
}