package controller.question;

abstract public class ViewQuestion 
{
	private final QuestionType type;
	private final String name;
		
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


