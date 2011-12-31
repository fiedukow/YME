package view;

import java.awt.Component;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

public class QuestionPanel extends JPanel
{
	View father;
	Vector<QuestionComponent> questions;
	
	QuestionPanel( View father )
	{
		super();
		this.father = father;
		repaint();
	}
}

abstract class QuestionComponent
{
	HashMap<String, Component> components;
	String name;	
}
