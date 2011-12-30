package controller.event;

/**
 * Type of event to recognize derived type easy
 * Yeah its ugly, but Java Class objects cant do same job, because they cannot be recognized after closing in 
 * base class type container :|
 * @author fiedukow
 *
 */
public enum EventType {
	MAP_POINT_SELECT, MAP_POINT_ACCEPT, CHANGE_FOCUS, UNDO, REDO, QUESTION_ANSWERED, TOOL_SELECT, LOAD_MAP, SAVE_MAP
}
