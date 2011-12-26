package model;
import java.util.Vector;

final class EditorToolbox {	
	CommandStack undo; /*Stack of commands invoked - user for "undo" operation*/
	CommandStack redo; /*Stack used for "redo" operation*/	
	
	/* invoke command and push on undo stack */
	void doCommand( Command cmd )
	{
		
	}
	void undo( int howMany ) throws CommandStackEmptyException
	{
		/*FIXME*/
	}
	void undo( ) throws CommandStackEmptyException
	{
		undo(1);
	}
	void redo( int howMany ) throws CommandStackEmptyException
	{
		/*FIXME*/
	}
	void redo( ) throws CommandStackEmptyException
	{
		redo(1);
	}
}

/*
 * Collects all Commands used by user in order to let user undo their operations
 * It's a stack of commands already invoken. 
 */
final class CommandStack
{	
	int push( Command cmd)
	{
		/*FIXME*/
		return 0;
	}
	Command pop( )
	{
		/*FIXME*/
		return new doDrawPolygon();
	}
	int push( Vector<Command> cmds )
	{
		/*FIXME*/
		return 0;
	}
	Vector<Command> pop( int howMany )
	{
		/*FIXME*/
		return new Vector<Command>();
	}
}

/*base class for all commands*/
interface Command
{	
	abstract void invoke() throws CommandInvokeException;
	abstract void undo() throws CommandUndoException;
}

class doDrawPolygon implements Command
{
	public void invoke() throws CommandInvokeException
	{
		/*FIXME*/
	}
	public void undo() throws CommandUndoException
	{
		/*FIXME*/
	}
}

class doResizePolygon implements Command
{
	public void invoke() throws CommandInvokeException
	{
		/*FIXME*/
	}
	public void undo() throws CommandUndoException
	{
		/*FIXME*/
	}	
}

class doRemovePolygon implements Command
{
	public void invoke() throws CommandInvokeException
	{
		/*FIXME*/
	}
	public void undo() throws CommandUndoException
	{
		/*FIXME*/
	}	
}

class doChangePolygonAttribute implements Command
{
	public void invoke() throws CommandInvokeException
	{
		/*FIXME*/
	}
	public void undo() throws CommandUndoException
	{
		/*FIXME*/
	}	
}

class doChangeMapAttribute implements Command
{
	public void invoke() throws CommandInvokeException
	{
		/*FIXME*/
	}
	public void undo() throws CommandUndoException
	{
		/*FIXME*/
	}	
}




class doShift implements Command
{
	public void invoke() throws CommandInvokeException
	{
		/*FIXME*/
	}
	public void undo() throws CommandUndoException
	{
		/*FIXME*/
	}	
}



/*
 *
 * 		EXCEPTION CLASSES BELOW
 * 
 */

/*throw when something goes wrong in command*/
class CommandInvokeException extends Exception 
{	
}

/*throw when something goes wrond in command undo*/
class CommandUndoException extends Exception
{
}

/*throw when user want to pop from CommandStack more then is available*/
final class CommandStackEmptyException extends Exception
{
}