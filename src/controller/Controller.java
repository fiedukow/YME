package controller;

import java.io.IOException;

import model.Model;
import view.View;

/*Controller in MVC meaning*/
public class Controller {
	Model model;
	View view;
	public Controller ( Model model, View view )
	{
		this.model = model;
		this.view = view;
		System.out.println("Controller created!");
		model.loadMap("maps/sample.xml");
		try {
			model.saveMap("maps/sample1.xml");
		} catch (IOException e) {
			System.err.println("Nie udalo sie zapisac mapy!");
		}
	}
}
