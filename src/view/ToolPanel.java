package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import controller.Tool;
import controller.event.EventToolSelect;

/**
 * Panel with basic tools to use on the map
 * Creates those buttons and action for them
 * @author fiedukow
 */
public class ToolPanel extends JPanel 
{
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The View inside which is this panel 
	 */
	private final View father;
	
	/**
	 * Map of buttons with tools
	 */
	private final LinkedHashMap<Tool, ToolButton> buttons;
	
	/**
	 * Main constructor, sets father and creates all ToolButtons which are necessary
	 * It also adds those buttons into Panel in the end.
	 * Some "hardcoded" things in here
	 * @param father - the parent View
	 */
	public ToolPanel( View father )
	{
		super();
		this.setLayout(new GridLayout(2,3));
		this.setPreferredSize( new Dimension(192,128) );
		this.setBorder(new TitledBorder("Narzedzia") );
		buttons = new LinkedHashMap<Tool, ToolButton>();
		this.father = father;
		
		{
			buttons.put(Tool.SELECTOR, new ToolButton("Wybierz obiekt na mapie", "icons/arrow.png", Tool.SELECTOR, father));
			buttons.put(Tool.STARTPOINT, new ToolButton("Rysuj punkt startowy lodzi", "icons/compass.png", Tool.STARTPOINT, father));
			buttons.put(Tool.POLYGON, new ToolButton("Rysuj dowolny poligon", "icons/polygon.png", Tool.POLYGON, father));
			buttons.put(Tool.RECTANGLE, new ToolButton("Rysuj prostokat", "icons/rectangle.png", Tool.RECTANGLE, father));
			buttons.put(Tool.ELLIPSE, new ToolButton("Rysuj elipse", "icons/ellipse.png", Tool.ELLIPSE, father));
			buttons.put(Tool.QUEY, new ToolButton("Rysuj keje", "icons/quey.png", Tool.QUEY, father));
		}
		
		for( ToolButton tb : buttons.values() )
		{
			this.add(tb);
		}
	}
	
	/**
	 * Mark selected tool with Color.red border
	 * @param tool - which tool button should be marked (by Tool identifier)
	 */
	public void setSelected( Tool tool )
	{
		for( ToolButton tb : buttons.values() )
		{	
			tb.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		ToolButton tmp = buttons.get(tool);
		if( tmp != null )
			tmp.setBorder(BorderFactory.createLineBorder(Color.red));			
	}
	
	/**
	 * Repaints this Panel. It call super.paintComponent and setSelected 
	 * using selected tool from snapshot provided by father.
	 */
	public void paintComponent( Graphics g )
	{
		/*it can be called in super()*/									
		setSelected( father.getState().getSelectedTool() );
		super.paintComponent(g); 
	}
}

/**
 * This class provides some basic functionality for all tool buttons.
 * eg. all ToolButtons already have default ActionListener
 * @author fiedukow
 */
class ToolButton extends JButton
{
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the parrent View - used to push event into this 
	 */
	final View father;
	
	/**
	 * Tool described by this button
	 */
	Tool tool;
	
	/**
	 * Standard constructor setting main fields and creating JButton
	 * @param message - hint for button
	 * @param iconPath - path for icon
	 * @param tool_ - tool described by this button
	 * @param father_ - the parent View
	 */
	ToolButton( String message, String iconPath, Tool tool_, View father_ )
	{
		super(new ImageIcon(iconPath));
		this.tool = tool_;
		this.father = father_;
		
		this.setToolTipText(message);
			
		this.addActionListener(
		new ActionListener()
        { 
            public void actionPerformed(ActionEvent e)
            {
            	father.pushEvent( new EventToolSelect( tool ) );
            }
        }); 
	}		
}