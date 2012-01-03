package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.question.ViewQuestion;

public class AttributePanel extends JPanel 
{
	Vector<AttributesLabel> currentLabels;	
	View father;
	AttributePanel( View father )
	{
		this.father = father;
	}
	public void paintComponent(Graphics g)
	{
		currentLabels = new Vector<AttributesLabel>(); 
		for( ViewQuestion q : father.getState().getQuestion() )
		{
			currentLabels.add(produceLabel(q));
		}		
		
		for( AttributesLabel label : currentLabels )
		{
			label.repaintAll();
		}
	}
	
	/*package*/AttributesLabel produceLabel( ViewQuestion question )
	{
		return new FileLabel("kotek");
	}
}

abstract class AttributesLabel
{
	abstract void repaintAll();
	abstract void addAll( Container toAddTo );
}

class FileLabel extends AttributesLabel
{
	JTextField fileName;
	JButton browseButton;
	
	FileLabel( String fileName )
	{
		this.fileName = new JTextField( fileName );
		this.browseButton = new JButton("...");
	}
	
	FileLabel( )
	{
		this.fileName = new JTextField("");
		this.browseButton = new JButton("...");
	}
	
	void repaintAll()
	{		
		fileName.repaint();
		browseButton.repaint();
	}
	
	void addAll( Container toAddTo )
	{
		toAddTo.add(fileName);
		toAddTo.add(browseButton);
	}
}