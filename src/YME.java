import java.io.IOException;

import view.View;

import controller.Controller;
import model.Model;

/*Main class*/
public class YME {
	public static final void main( String[] args )
	{		
		System.out.println("Witaj w YME :-)");		
		Model md = new Model();
		View v = new View();
		new Controller(md,v);
	}
}