package controller.question;

public class StringValueQuestion extends ViewQuestion 
{
	private final String value;
	public StringValueQuestion( String name, QuestionType type, String value ) throws WrongQuestionTypeException
	{
		super(name,type);
		
		if( type != QuestionType.STRING ) throw new WrongQuestionTypeException();
		this.value = value;
	}
	
	StringValueQuestion( String name, QuestionType type )
	{
		super(name,type);
		this.value = ""; 
	}
	
	public String getValue()
	{
		return this.value;
	}
}
