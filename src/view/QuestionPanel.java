package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import model.TypeOfMapObject;

import controller.FocusType;
import controller.event.EventQuestionAnswered;
import controller.question.ActionQuestion;
import controller.question.DoubleIntValueQuestion;
import controller.question.StringValueQuestion;
import controller.question.QuestionType;
import controller.question.TypeOfMapObjectQuestion;
import controller.question.ViewQuestion;
import controller.question.WrongQuestionTypeException;

/**
 * Panel with attributes of current focus
 * It contains components needed to answer questions about current focus
 * asked by Controller.
 * @author fiedukow
 */
public class QuestionPanel extends JPanel
{
	/**
	 * UID 
	 */
	private static final long serialVersionUID = 1L;
	private final FocusType myFocusType;
	private final Integer   myFocusId;
	
	/**
	 * Map which translate Controller questionName into user readable form
	 */
	private static final HashMap<String, String> translator = new HashMap<String, String>();
	{	
		translator.put("texture" 		, "Tekstura");
		//TODO, it shoud be better then this ---
		translator.put("delete" 	 	, "                  Usuń                  ");
		translator.put("mapName" 		, "Nazwa mapy");
		translator.put("size"    	 	, "Wymiary");
		translator.put("position"       , "Pozycja");
		translator.put("typeOfMapObject", "Typ obiektu");	
	}
	
	/**
	 * the View which this component is part of.
	 */
	View father;
	
	/**
	 * Contains all Components of questions asked by Controller.
	 * @see QuestionComponent
	 */
	ArrayList<QuestionComponent> questions;
	
	/**
	 * Main constructor - set father and ask him for question from current snapshot
	 * It creates all needed components and display it on the panel.
	 * @param father
	 */
	QuestionPanel( View father )
	{
		super();
		this.father = father; 
		this.myFocusType = father.getState().getFocusType();
		this.myFocusId = father.getState().getFocusId();
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
	
	boolean acctual( FocusType focusType, Integer focusId )
	{
		return ( focusType == myFocusType && focusId == myFocusId );
	}
	
	/**
	 * Accessor to translate table.
	 * @param toTranslate - Controller questionName.
	 * @return user readable name of question
	 */
	public static String Translate( String toTranslate )
	{
		return translator.get(toTranslate);
	}
}
	
/**
 * Factory of QuestionComponents
 * It allows to create QuestionComponents objects according to ViewQuestion object 
 * @author fiedukow
 */
class QuestionComponentFactory
{
	/**
	 * Object ready to use after constructor of this class
	 * It is the QuestionComponent created by factory.
	 */
	QuestionComponent created;
	
	/**
	 * Main constructor - create object using question
	 * It also provides father object required by all derived by QuestionComponent classes 
	 * @param question - question asked by Controller
	 * @param father - the View object - provides current snapshot
	 */
	public QuestionComponentFactory( ViewQuestion question, View father )
	{
		switch( question.getType() )
		{
			case TWICE_INT:
				int values[] = ((DoubleIntValueQuestion) question).getValue();
				created = new QuestionTwiceIntComponent( father, question.getName() , values[0] , values[1] ); 
				break;
			case STRING:
				String value = ((StringValueQuestion) question).getValue();
				created = new QuestionStringComponent( father, question.getName(), value );
				break;
			case BUTTON:
				created = new QuestionButtonComponent( father, question.getName() );
				break;
			case TYPE_OF_MAP_OBJECT:
				TypeOfMapObject selected = ((TypeOfMapObjectQuestion) question).getValue();							
				LinkedList<TypeOfMapObject> possibleValues = ((TypeOfMapObjectQuestion) question).getPossibleValues();
				created = new QuestionTypeOfMapObjectComponent( father, question.getName(), selected, possibleValues );
				break;
			default:
				created = new QuestionTwiceIntComponent( father, question.getName() , 0, 0 );
				break;
		}
	}
	
	/**
	 * Access to object created by factory
	 * @return created object
	 */
	public QuestionComponent getObject()
	{
		return created;
	}
}

/**
 * Base class for all Question components
 * Question component contains number of Swing components ready to add into panel
 * @author fiedukow
 *
 */
abstract class QuestionComponent
{
	/**
	 * Swing component ready to use
	 */	
	LinkedList< Component > componentsToDraw;
	
	/**
	 * Name of the question given by Controller
	 */
	protected final String name;
	
	/**
	 * the View - provides push event & snapshot
	 */
	protected final View father;
	
	/**
	 * Main constructor - used in derived classes as super
	 * It set field common for all derived classes
	 * @param father - View providing event pushing & snapshot
	 * @param name - name of question given by Controller.
	 */
	protected QuestionComponent(View father, String name)
	{
		this.name = name;
		this.father = father;
		componentsToDraw = new LinkedList <Component>();
	}
	
	/**
	 * Gives collection of swing compononents
	 */
	public Collection<Component> getComponents()
	{
		return componentsToDraw;
	}
	
	/**
	 * Static method operating on string 
	 * It cut&pad string to given length
	 * @param toPad - String to pad
	 * @param size - place size
	 * @return - padded String
	 */
	static String padding( String toPad, int size )
	{
		return String.format("%-" + size + "s", toPad.substring(0,Math.min(toPad.length(), size )));
	}
	
	/**
	 * Should send answer to Controller
	 */
	abstract void sendEvent();
}


/**
 * Component with label and place for two int elements
 * [LABEL9chars] : [INT] [INT]
 * @author fiedukow
 */
class QuestionTwiceIntComponent extends QuestionComponent
{	
	JTextField firstTF, secondTF;
	
	QuestionTwiceIntComponent( View father, String name, int first, int second )	
	{
		super( father, name );
		firstTF = new JTextField(""+first, 5);		
		secondTF = new JTextField( ""+second, 5 );
		JLabel description = new JLabel( padding( QuestionPanel.Translate(name), 9 )+": " );
		description.setFont( new Font ( "Courier", Font.PLAIN, 14 ) );
		componentsToDraw.add( description );
		componentsToDraw.add( firstTF );
		componentsToDraw.add( secondTF );	
		
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





class QuestionStringComponent extends QuestionComponent
{	
	JTextField valueField;
	
	QuestionStringComponent( View father_, String name, String value )	
	{
		super( father_, name );
		valueField = new JTextField(value, 11);	
		JLabel description = new JLabel( padding( QuestionPanel.Translate(name) , 9 )+": " );
		description.setFont( new Font ( "Courier", Font.PLAIN, 14 ) );
		componentsToDraw.add( description );
		componentsToDraw.add( valueField );
		
		ActionListener actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent event) {
				sendEvent();
			}
		};
		
		valueField.addActionListener( actionListener );
	}
	void sendEvent(){
		try
		{
			father.pushEvent( 
						new EventQuestionAnswered( 
							new StringValueQuestion( 
								name, 
								QuestionType.STRING, 
								valueField.getText()
								) 
						     ) 
						);
			
		}		
		catch( WrongQuestionTypeException e )
		{
			father.showInfo("BŁĄD KRYTYCZNY: Niepoprawny typ pytania kontrolera!\n");
			throw new RuntimeException();
		}
	}
}


class QuestionButtonComponent extends QuestionComponent
{		
	JButton action;
	
	QuestionButtonComponent( View father, String name )	
	{
		super( father, name );
		action = new JButton( QuestionPanel.Translate(name) );
		action.setMnemonic( KeyEvent.VK_R ); //ALT+R to remove element
		componentsToDraw.add( action );		
		
		ActionListener actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent event) {
				sendEvent();
			}
		};
		
		action.addActionListener( actionListener );
	}
	void sendEvent(){
		try
		{
			father.pushEvent( 
						new EventQuestionAnswered( 
							new ActionQuestion( 
								name,
								QuestionType.BUTTON
								) 
						     ) 
						);
			
		}		
		catch( WrongQuestionTypeException e )
		{
			father.showInfo("BŁĄD KRYTYCZNY: Niepoprawny typ pytania kontrolera!\n");
			throw new RuntimeException();
		}
	}
}

class QuestionTypeOfMapObjectComponent extends QuestionComponent
{		
	JComboBox<String> box;
	private final HashMap<String, TypeOfMapObject> rePadMap;
	
	QuestionTypeOfMapObjectComponent( View father, String name, TypeOfMapObject selectedValue, LinkedList<TypeOfMapObject> possibleValues )	
	{
		super( father, name ); 
		rePadMap = new HashMap<String, TypeOfMapObject>();
						
		JLabel description = new JLabel( padding( QuestionPanel.Translate(name) , 9 )+": " );
		description.setFont( new Font ( "Courier", Font.PLAIN, 14 ) );
		componentsToDraw.add( description );
		
		box = new JComboBox<String>( );
		
		for( TypeOfMapObject value : possibleValues )
		{
			String show = padding( value.toString(), 14 ); 
			rePadMap.put( show, value);
			box.addItem( show );
		}
		box.setSelectedItem(  padding( selectedValue.toString(), 14 ) );
		
		componentsToDraw.add( box );		
		
		ItemListener itemListener = new ItemListener()
		{
			public void itemStateChanged(ItemEvent arg0) {
				sendEvent();
			}
		};
		
		box.addItemListener( itemListener );
	}
	
	void sendEvent(){	
		try
		{
			father.pushEvent( 
						new EventQuestionAnswered( 
							new TypeOfMapObjectQuestion( 
								name,
								QuestionType.TYPE_OF_MAP_OBJECT,
								unPad((String) box.getSelectedItem()),
								new LinkedList<TypeOfMapObject>()
								)
						     ) 
						);
			
		}		
		catch( WrongQuestionTypeException e )
		{
			father.showInfo("BŁĄD KRYTYCZNY: Niepoprawny typ pytania kontrolera!\n");
			throw new RuntimeException();
		}
	}
	
	private TypeOfMapObject unPad( String key )
	{
		return rePadMap.get( key );
	}
}
