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
import javax.swing.border.TitledBorder;

import controller.FocusType;
import controller.event.EventChangeFocus;
import controller.event.EventRedo;
import controller.event.EventToolSelect;
import controller.event.EventUndo;

import model.MapShape;

public class NavigatePanel extends JPanel  
{
	JButton nextFocus, prevFocus, undo, redo;
	JComboBox shapeList;
	View father;
	/*it shouldn't be here - it's most ugly thing i ever done :(*/
	boolean consumeEvent;
	
	public NavigatePanel( View father_ )
	{
		super();		
		consumeEvent = false;
		father = father_;
		nextFocus = new JButton(">");
		prevFocus = new JButton("<");
		undo = new JButton("< Cofnij");		
		redo = new JButton("Ponow  >");
		
		this.setBorder(new TitledBorder("Nawigacja"));
		
		undo.setEnabled( father.getState().isUndoNotEmpty() );		
		redo.setEnabled( father.getState().isRedoNotEmpty() );	
		
		shapeList = new JComboBox();
		shapeList.addItem(new FocusListElement("Cała mapa", FocusType.MAP, null ));
		shapeList.addItem(new FocusListElement("Punkt startowy", FocusType.START_POINT, null));		
		shapeList.setFont(new Font("Courier", Font.PLAIN, 12));
		int i = 1;
		for( MapShape sh : father.getState().getMap().getShapes())
		{
			shapeList.addItem(new FocusListElement("Element # "+(i+1), FocusType.SHAPE, i ));
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
		
		redo.addActionListener(
				new ActionListener()
		        { 
		            public void actionPerformed(ActionEvent e)
		            {
		            	father.pushEvent( new EventRedo( ) );
		            }
		});
		
		nextFocus.addActionListener(
				new ActionListener()
		        { 
		            public void actionPerformed(ActionEvent e)
		            {
		            	shapeList.setSelectedIndex( (shapeList.getSelectedIndex()+1)%shapeList.getItemCount() );
		            }
		});
		
		prevFocus.addActionListener(
				new ActionListener()
		        { 
		            public void actionPerformed(ActionEvent e)
		            {
		            	int selected = (shapeList.getSelectedIndex()-1)%shapeList.getItemCount();
		            	if( selected < 0 ) selected = shapeList.getItemCount()+selected;
		            	shapeList.setSelectedIndex( selected );
		            }
		});
		
		
	
		shapeList.addActionListener(
				new ActionListener()
		        { 
		            public void actionPerformed(ActionEvent e)
		            {
		            	FocusListElement item = (FocusListElement) shapeList.getSelectedItem();
		            	if( consumeEvent ) return;
		            	if( item==null ) return;
		            	if( father.getState().getFocusType() != item.getFocusType() || 
		            		father.getState().getFocusId() != item.getFocusId() )
		            	{
		            		father.pushEvent( new EventChangeFocus( item.getFocusType(), item.getFocusId() ));
		            	}
		            }
		        }				
		);
		
				
		
		this.add(prevFocus);
		this.add(shapeList);
		this.add(nextFocus);

		this.add(undo);		
		this.add(redo);		
		this.setPreferredSize(new Dimension(250, 200));
	}
	
	public void paintComponent(Graphics g)
	{
		consumeEvent = true;
		shapeList.removeAllItems();
	
		shapeList.addItem(new FocusListElement("Cała mapa", FocusType.MAP, null ));
		shapeList.addItem(new FocusListElement("Punkt startowy", FocusType.START_POINT, null));
		consumeEvent = false;
				
		int i = 0;
		for( MapShape sh : father.getState().getMap().getShapes())
		{
			shapeList.addItem( new FocusListElement("Element # "+(i+1), FocusType.SHAPE, i ));
			++i;
		}
			
		
		for( i = 0; i < shapeList.getItemCount(); ++i )
		{
			if ( ((FocusListElement) shapeList.getItemAt(i)).getFocusType() == father.getState().getFocusType() 
			&& ( 
				 (((FocusListElement) shapeList.getItemAt(i)).getFocusId() == null )||
				 (((FocusListElement) shapeList.getItemAt(i)).getFocusId() == father.getState().getFocusId() )) 
			   )
			{				
				shapeList.setSelectedIndex(i);
				break;
			}
		}
		
		undo.setEnabled( father.getState().isUndoNotEmpty() );
		redo.setEnabled( father.getState().isRedoNotEmpty() );
		
		super.paintComponent(g);
	}
}

class FocusListElement
{
	String label;
	FocusType focusType;
	Integer focusId;
	
	public FocusListElement( String label, FocusType focusType, Integer focusId )
	{
		this.label = label;
		this.focusType = focusType;
		this.focusId = focusId;
	}
	
	/**
	 * @return the focusType
	 */
	public FocusType getFocusType() {
		return focusType;
	}

	/**
	 * @return the focusId
	 */
	public Integer getFocusId() {
		return focusId;
	}
	
	public String toString()
	{
		return label;
	}
}