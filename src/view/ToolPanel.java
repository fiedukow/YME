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

public class ToolPanel extends JPanel 
{
	View father;
	LinkedHashMap<Tool, ToolButton> buttons;
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
	
	/*choose tool*/
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
	
	public void paintComponent( Graphics g )
	{
		/*it can be called in super()*/									
		setSelected( father.getState().getSelectedTool() );
		super.paintComponent(g); 
	}
}


class ToolButton extends JButton
{
	final View father;
	Tool tool;
	
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