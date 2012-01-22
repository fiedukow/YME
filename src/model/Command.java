package model;
/**
 * Base class for all commands. (Command design pattern).
 * Warning: UNDO command will work only if the map state is the same as just after using invoke!
 * @author fiedukow
 */

//TODO, SHOULD BE IN OTHER PACKAGE! 
public interface Command
{	
	abstract void invoke( EditorMap map ) throws CommandInvokeException;
	abstract void undo( EditorMap map ) throws CommandUndoException;
}


/*
 * 
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

