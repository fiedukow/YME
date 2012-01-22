package model;
import java.util.Stack;

/**
 * Main class used to change EditorMap state.
 * @author fiedukow
 */
public final class EditorToolbox 
{	
	final private Stack<Command> undo; /*Stack of commands invoked - user for "undo" operation*/
	final private Stack<Command> redo; /*Stack used for "redo" operation*/
	final private EditorMap map;
	
	public EditorToolbox( EditorMap map )
	{
		undo = new Stack<Command>();
		redo = new Stack<Command>();	
		this.map = map;
	}
	
	/**
	 * Invoke command and then push it on undo stack
	 * @param cmd - command to invoke
	 */
	public void doCommand( Command cmd ) 
	{
		try 
		{
			cmd.invoke( map );
		} 
		catch (CommandInvokeException e) 
		{
			System.err.println("Nie udalo sie wykonac komendy");
			return;
		}
		undo.push(cmd);
		redo.clear();
	}
	
	/**
	 * Pop some command from undo stack and undo them.
	 * @param howMany - how many command do you want to undo
	 * @throws CommandStackEmptyException When someone tries to pop more then it's available
	 */
	public void undo( int howMany ) throws CommandStackEmptyException
	{
		Command cmd;
		for(int i = 0; i < howMany; ++i )
		{
			cmd  = undo.pop();
			try {
				cmd.undo( map );				
			}
			catch (CommandUndoException e) 
			{
				System.err.println("Nie udalo sie wycofać komendy");
				undo.push(cmd); /* put it back where it was */
				return;
			}
			redo.push(cmd);
		}
	}
	
	/**
	 * Pop one command from undo stack and undo it.
	 * @throws CommandStackEmptyException
	 * @see EditorToolbox#undo(int)
	 */
	public void undo( ) throws CommandStackEmptyException
	{
		undo(1);
	}
	
	/**
	 * Pop some command from redo stack and ivoke them.
	 * @param howMany - how many command do you want to invoke
	 * @throws CommandStackEmptyException When someone tries to pop more then it's available
	 */
	public void redo( int howMany ) throws CommandStackEmptyException
	{
		Command cmd;
		for(int i = 0; i < howMany; ++i )
		{
			cmd  = redo.pop();
			try 
			{
				cmd.invoke( map );
			} 
			catch (CommandInvokeException e) 
			{
				System.err.println("Nie udalo sie wykonac ponownie komendy");
				redo.push(cmd); /* put it back where it was */
				return;
			}
			undo.push(cmd);
		}
	}
	
	/**
	 * Pop one command from redo stack and invoke it.
	 * @throws CommandStackEmptyException
	 * @see EditorToolbox#redo(int)
	 */
	public void redo( ) throws CommandStackEmptyException
	{
		redo(1);
	}
	
	public int getUndoSize()
	{
		return undo.size();
	}
	
	public int getRedoSize()
	{
		return redo.size();
	}
	
}

