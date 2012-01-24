package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import controller.FocusType;
import controller.event.EventChangeFocus;
import controller.event.EventRedo;
import controller.event.EventUndo;

public class NavigatePanel extends JPanel  
{
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
	private final JButton nextFocus, prevFocus;

	final JButton undo;
	final JButton redo;
	
	private final JComboBox<FocusListElement> shapeList;
	private final JProgressBar BQFill;
	private final View father;
	
	/*it shouldn't be here - it's most ugly thing i ever done :(*/
	private boolean consumeEvent;
	
	public NavigatePanel( View father_ )
	{
		super();		
		consumeEvent = false;
		father = father_;
		nextFocus = new JButton(">"); 
		prevFocus = new JButton("<");
		undo = new JButton("< Cofnij");		
		redo = new JButton("Ponow  >");
		
		BQFill = new JProgressBar(0, 128);
		BQFill.setValue(father.eventQueue.size());
		new BQUpdater( BQFill, father );
		
		this.setBorder(new TitledBorder("Nawigacja"));
		
		undo.setEnabled( father.getState().isUndoNotEmpty() );		
		redo.setEnabled( father.getState().isRedoNotEmpty() );	
		
		shapeList = new JComboBox<FocusListElement>();
		shapeList.addItem(new FocusListElement("Cała mapa", FocusType.MAP, null ));
		shapeList.addItem(new FocusListElement("Punkt startowy", FocusType.START_POINT, null));		
		shapeList.setFont(new Font("Courier", Font.PLAIN, 12));
		int size = father.getState().getShapesCount();
		for( int i  = 0; i < size; ++i )
			shapeList.addItem(new FocusListElement("Element # "+(i+1), FocusType.SHAPE, i ));			
		
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
		
		this.add(BQFill);
		
		this.setPreferredSize(new Dimension(250, 200));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		consumeEvent = true;
		shapeList.removeAllItems();
	
		shapeList.addItem(new FocusListElement("Cała mapa", FocusType.MAP, null ));
		shapeList.addItem(new FocusListElement("Punkt startowy", FocusType.START_POINT, null));
		consumeEvent = false;	
		
		int size = father.getState().getShapesCount();
		for( int i = 0; i < size; ++i )
			shapeList.addItem( new FocusListElement("Element # "+(i+1), FocusType.SHAPE, i ));
			
		
		for( int i = 0; i < shapeList.getItemCount(); ++i )
		{
			if ( ((FocusListElement) shapeList.getItemAt(i)).getFocusType() == father.getState().getFocusType() 
			&& ( 
				 (((FocusListElement) shapeList.getItemAt(i)).getFocusId() == null ) ||
				 (((FocusListElement) shapeList.getItemAt(i)).getFocusId() == father.getState().getFocusId() )) 
			   )
			{				
				shapeList.setSelectedIndex(i);
				break;
			}
		}
		
		undo.setEnabled( father.getState().isUndoNotEmpty() );
		redo.setEnabled( father.getState().isRedoNotEmpty() );		
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

/**
 * Status of blocking Queue - it updates provided progress bar presenting fill of the BlockingQueue
 * @author fiedukow
 */
class BQUpdater extends Thread
{
	/**
	 * ProgressBar component presenting blocking queue fill
	 */
	JProgressBar BQFill;
	
	/**
	 * View objest which contains this element.
	 */
	View father;
	
	/**
	 * Simple constructor setting internal field correctly and start updater thread.
	 * @param BQFill - Graphics component which should be updated by BQUpdater
	 * @param father - View which blocking Queue will be observed
	 */
	public BQUpdater( JProgressBar BQFill, View father )
	{
		this.BQFill = BQFill;
		this.father = father;
		start();
	}
	
	/**
	 * Update statusBar 20 times/ sec
	 */
	public void run()
	{
		while( true )
		{			
			BQFill.setValue(father.eventQueue.size());			
			if( (double) BQFill.getValue()/BQFill.getMaximum() < 0.5 )
				BQFill.setForeground(Color.GREEN);
			else if( (double) BQFill.getValue()/BQFill.getMaximum() < 0.7 )
				BQFill.setForeground(Color.YELLOW);
			else if( (double) BQFill.getValue()/BQFill.getMaximum() < 0.9 )
				BQFill.setForeground(Color.ORANGE);
			else if( (double) BQFill.getValue()/BQFill.getMaximum() < 1 )
				BQFill.setForeground(Color.RED);
			else 
				BQFill.setForeground(Color.BLACK);
			
			BQFill.repaint();
			try {
				sleep(50);
			} catch (InterruptedException e) {
				System.err.println("BlockingQueue progress bar awainting interrupted.");
			}
		}
	}
}