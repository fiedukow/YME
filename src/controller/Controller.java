package controller;

import model.Model;

/*Controller in MVC meaning*/
public class Controller {
	Model model;
	public Controller ( Model model )
	{
		this.model = model;
		System.out.println("Controller created!");
	}
}
