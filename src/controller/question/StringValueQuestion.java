package controller.question;

public class StringValueQuestion extends ViewQuestion 
{
	String value;
	public StringValueQuestion( String name, QuestionType type, String value )
	{
		super(name,type);
		this.value = value;
	}
	
	StringValueQuestion( String name, QuestionType type )
	{
		super(name,type);
		this.value = ""; 
	}
}
