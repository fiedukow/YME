package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import controller.FocusType;
import controller.event.EventToolSelect;
import controller.event.EventUndo;

import model.MapShape;

public class NavigatePanel extends JPanel  
{
	JButton nextFocus, prevFocus, undo, redo;
	JComboBox shapeList;
	View father;
	
	public NavigatePanel( View father_ )
	{
		super();		
		father = father_;
		nextFocus = new JButton(">");
		prevFocus = new JButton("<");
		undo = new JButton("<-");		
		redo = new JButton("->");
		
		undo.setEnabled( father.getState().isUndoNotEmpty() );		
		redo.setEnabled( father.getState().isRedoNotEmpty() );	
		
		shapeList = new JComboBox();
		shapeList.addItem("Cała mapa");
		shapeList.addItem("Start łodzi");		
		shapeList.setFont(new Font("Courier", Font.PLAIN, 12));
		int i = 1;
		for( MapShape sh : father.getState().getMap().getShapes())
		{
			shapeList.addItem("Element #"+i );
			++i;
		}
		
		undo.addActionListener(
		new ActionListener()
        { 
            public void actionPerformed(ActionEvent e)
            {
            	father.pushEvent( new EventUndo( ) );
            }
        }); 
		
		
		
		
		this.add(prevFocus);
		this.add(shapeList);
		this.add(nextFocus);

		this.add(undo);		
		this.add(redo);		
		this.setPreferredSize(new Dimension(250, 200));
	}
	
	public void paintComponent(Graphics g)
	{		
		shapeList.removeAllItems();
		shapeList.addItem("Cała mapa");
		shapeList.addItem("Start łodzi");
		int i = 1;
		for( MapShape sh : father.getState().getMap().getShapes())
		{
			shapeList.addItem("Element #"+i );
			++i;
		}
		if( father.getState().getFocusType()==FocusType.MAP)
			shapeList.setSelectedIndex(0);
		else if( father.getState().getFocusType()==FocusType.START_POINT)
			shapeList.setSelectedIndex(1);
		else if( father.getState().getFocusType()==FocusType.SHAPE)
			shapeList.setSelectedIndex(father.getState().getFocusId()+2);
		
		undo.setEnabled( father.getState().isUndoNotEmpty() );
		redo.setEnabled( father.getState().isRedoNotEmpty() );
		
		super.paintComponent(g);
	}
}
