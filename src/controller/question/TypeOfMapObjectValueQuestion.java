package controller.question;

import model.TypeOfMapObject;

public class TypeOfMapObjectValueQuestion extends ViewQuestion 
{
	TypeOfMapObject value;
	TypeOfMapObjectValueQuestion( String name, QuestionType type, TypeOfMapObject value ) throws WrongQuestionTypeException
	{
		super(name,type);
		
		if( type != QuestionType.TYPE_OF_MAP_OBJECT ) throw new WrongQuestionTypeException();
		this.value = value;
	}	
	
	public TypeOfMapObject getValue()
	{
		return this.value;
	}
}
