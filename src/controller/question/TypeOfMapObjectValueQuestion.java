package controller.question;

import model.TypeOfMapObject;

public class TypeOfMapObjectValueQuestion extends ViewQuestion 
{
	TypeOfMapObject value;
	TypeOfMapObjectValueQuestion( String name, QuestionType type, TypeOfMapObject value )
	{
		super(name,type);
		this.value = value;
	}	
}
