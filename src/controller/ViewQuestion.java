package controller;

public class ViewQuestion 
{
	QuestionType type;
	String name;
	public ViewQuestion( String name, QuestionType type )
	{
		this.type = type;
		this.name = name;	
	}
	
	/**
	 * @return the type
	 */
	public QuestionType getType() {
		return type;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}	
}
