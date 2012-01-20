package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import controller.event.EventQuestionAnswered;
import controller.question.DoubleIntValueQuestion;
import controller.question.QuestionType;
import controller.question.ViewQuestion;
import controller.question.WrongQuestionTypeException;

public class QuestionPanel extends JPanel
{
	View father;
	Vector<QuestionComponent> questions;
	
	QuestionPanel( View father )
	{
		super();
		this.father = father; 
		this.setBorder(new TitledBorder("Atrybuty"));
		this.setPreferredSize(new Dimension(200, 200) );
		
		for( ViewQuestion question : father.getState().getQuestions() )
		{	
			QuestionComponent qc = new QuestionComponentFactory( question, father ).getObject();			
			for( Component comp : qc.getComponents() )
			{				
				this.add(comp);
			}			
		}	
		this.setVisible(true);
	}
}
	
class QuestionComponentFactory
{
	QuestionComponent created;
	public QuestionComponentFactory( ViewQuestion question, View father )
	{
		switch( question.getType() )
		{
			case TWICE_INT:
				int v[] = ((DoubleIntValueQuestion) question).getValue();
				created = new QuestionTwiceInt( father, question.getName() , v[0] , v[1] ); 
				break;
			default:
				created = new QuestionTwiceInt( father, question.getName() , 99,99 );
				break;
		}
	}
	
	public QuestionComponent getObject()
	{
		return created;
	}
}

abstract class QuestionComponent
{
	HashMap<String, Component> componentsAnswers;
	LinkedList< Component > componentsToDraw;
	String name;
	View father;
	abstract void sendEvent();
	QuestionComponent(View father, String name)
	{
		this.name = name;
		this.father = father;
		componentsAnswers = new HashMap<String, Component>();
		componentsToDraw = new LinkedList <Component>();
	}
	Collection<Component> getComponents()
	{
		return componentsToDraw;
	}
	
	static String padding( String toPad, int size )
	{
		return String.format("%-" + size + "s", toPad.substring(0,Math.min(toPad.length(), size )));
	}
}

class QuestionTwiceInt extends QuestionComponent
{	
	JTextField firstTF, secondTF;
	
	QuestionTwiceInt( View father, String name, int first, int second )	
	{
		super( father, name );
		firstTF = new JTextField(""+first, 5);		
		secondTF = new JTextField( ""+second, 5 );
		JLabel description = new JLabel( padding( name, 9 )+": " );
		description.setFont( new Font ( "Courier", Font.PLAIN, 14 ) );
		componentsToDraw.add( description );
		componentsToDraw.add( firstTF );
		componentsToDraw.add( secondTF );
		
		componentsAnswers.put("first", new JTextField( ""+first, 5 ));
		componentsAnswers.put("second", new JTextField( ""+second, 5 ));
		
		ActionListener forBoth = new ActionListener()
		{
			public void actionPerformed(ActionEvent event) {
				sendEvent();
			}
		};
		
		firstTF.addActionListener( forBoth );			
		secondTF.addActionListener( forBoth );
	}
	void sendEvent(){
		try
		{
			father.pushEvent( 
						new EventQuestionAnswered( 
							new DoubleIntValueQuestion( 
								name, 
								QuestionType.TWICE_INT, 
								Integer.decode( firstTF.getText() ), 
								Integer.decode( secondTF.getText() )
								) 
						     ) 
						);
			
		}
		catch( NumberFormatException e )
		{
			father.showInfo("BŁAD: Podales nieprawidlowe dane - uzyj wartosci calkowitoliczbowych.\n");
		}
		catch( WrongQuestionTypeException e )
		{
			father.showInfo("BŁĄD KRYTYCZNY: Niepoprawny typ pytania kontrolera!\n");
			throw new RuntimeException();
		}
	}
}