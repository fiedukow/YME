package model;

/**
 * Represent the type of object in final game.
 * @author fiedukow
 */
public enum TypeOfMapObject {
	 NO_OBJECT	, /*only for early initialization*/
	 QUAY		, /*quay, allowed for rectangle only*/
	 DESTROY	, /*object which destroy the boat in the case of collision */
	 STOP		, /*object which stop the boat in the case of collision */
	 BUMP 		  /*object which bump the boat in the case of collision */
}

/**
 * Throw this Exception every time when someone tries to use forbidden (in any way) TypeOfMapObject
 * or when someone want to choose nonexistent value.
 * @author fiedukow
 */
class InvalidTypeOfMapObjectException extends Exception
{	
}