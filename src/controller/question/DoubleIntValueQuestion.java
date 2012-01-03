package controller.question;

public class DoubleIntValueQuestion extends ViewQuestion 
{
	int first, second;
	public DoubleIntValueQuestion( String name, QuestionType type, int first, int second )
	{
		super(name,type);
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
		int[] result = {first,second};
		return result;
	}
}
