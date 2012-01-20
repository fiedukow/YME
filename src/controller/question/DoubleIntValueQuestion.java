package controller.question;

public class DoubleIntValueQuestion extends ViewQuestion 
{
	int first, second;
	public DoubleIntValueQuestion( String name, QuestionType type, int first, int second ) throws WrongQuestionTypeException
	{
		super(name,type);
		
		if( type != QuestionType.TWICE_INT ) throw new WrongQuestionTypeException();
		this.first = first;
		this.second = second;
	}
	DoubleIntValueQuestion( String name, QuestionType type )
	{
		super(name,type);
		this.first = this.second = 0;
	}
	public int[] getValue()
	{
		int[] result = { first, second };
		return result;
	}
}
