package view;

import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenu extends JMenuBar 
{
	JMenu fileMenu;
	JMenu helpMenu;
	JMenu editMenu;
	HashMap<String, JMenuItem> options;
	View father;
	
	MainMenu(View father)
	{	
		this.father = father;
		fileMenu = new JMenu("Plik");
		editMenu = new JMenu("Edycja");
		helpMenu = new JMenu("Pomoc");
		options  = new HashMap<String, JMenuItem>(); 

		options.put("Plik/Nowy", new JMenuItem("Nowy")	);
		options.put("Plik/Otwórz", new JMenuItem("Otwórz")	);
		options.put("Plik/Zapisz jako", new JMenuItem("Zapisz jako")	);
		options.put("Plik/Zamknij", new JMenuItem("Zamknij")	);
		fileMenu.add(options.get("Plik/Nowy"));
		fileMenu.add(options.get("Plik/Otwórz"));
		fileMenu.add(options.get("Plik/Zapisz jako"));
		fileMenu.add(options.get("Plik/Zamknij"));
		
		options.put("Edycja/Cofnij", new JMenuItem("Confnij")	);
		options.put("Edycja/Powtórz", new JMenuItem("Powtórz")	);
		editMenu.add(options.get("Edycja/Cofnij"));
		editMenu.add(options.get("Edycja/Powtórz"));
		
		options.put("Pomoc/O programie...", new JMenuItem("O programie...")	);
		options.put("Pomoc/Pomoc", new JMenuItem("Pomoc")	);
		helpMenu.add(options.get("Pomoc/O programie..."));
		helpMenu.add(options.get("Pomoc/Pomoc"));
		
		this.add(fileMenu);
		this.add(helpMenu);
		this.add(editMenu);
	}

}
