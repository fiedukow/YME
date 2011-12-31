package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


import controller.event.EventExitProgram;
import controller.event.EventLoadMap;
import controller.event.EventNewMap;
import controller.event.EventSaveMap;
import controller.event.EventToolSelect;

public class MainMenu extends JMenuBar 
{
	JMenu fileMenu;
	JMenu helpMenu;
	JMenu editMenu;
	HashMap<String, JMenuItem> options;
	View father;
	JFileChooser saveFC;
	JFileChooser loadFC;
	
	MainMenu(View father)
	{	
		this.father = father;
		fileMenu = new JMenu("Plik");
		editMenu = new JMenu("Edycja");
		helpMenu = new JMenu("Pomoc");
		options  = new HashMap<String, JMenuItem>();
		saveFC 	 = new JFileChooser();		
		loadFC 	 = new JFileChooser();
		loadFC.setCurrentDirectory(new File("./maps"));
		loadFC.setFileFilter( new XMLFilter() );
		loadFC.setApproveButtonText("Otwórz");
		saveFC.setCurrentDirectory(new File("./maps"));
		saveFC.setFileFilter( new XMLFilter() );
		saveFC.setApproveButtonText("Zapisz");
		
		options.put("Plik/Nowy", new JMenuItem("Nowy")	);
		options.put("Plik/Otwórz", new JMenuItem("Otwórz")	);
		options.put("Plik/Zapisz jako", new JMenuItem("Zapisz jako")	);
		options.put("Plik/Zamknij", new JMenuItem("Zamknij")	);
		fileMenu.add(options.get("Plik/Nowy"));
		fileMenu.add(options.get("Plik/Otwórz"));			
		fileMenu.add(options.get("Plik/Zapisz jako"));		
		fileMenu.add(options.get("Plik/Zamknij"));
		
		options.put("Edycja/Cofnij", new JMenuItem("Cofnij")	);
		options.put("Edycja/Powtórz", new JMenuItem("Powtórz")	);
		editMenu.add(options.get("Edycja/Cofnij"));
		editMenu.add(options.get("Edycja/Powtórz"));
		
		options.put("Pomoc/O programie...", new JMenuItem("O programie...")	);
		helpMenu.add(options.get("Pomoc/O programie..."));

		addEvents();
		
		this.add(fileMenu);		
		this.add(editMenu);
		this.add(helpMenu);
	}
	
	private void addEvents()
	{
		options.get("Plik/Otwórz").addActionListener(
					new ActionListener()
					{ 
						public void actionPerformed(ActionEvent e)
						{
							int FCOption = loadFC.showOpenDialog((JMenuItem)e.getSource());
							if (FCOption == JFileChooser.APPROVE_OPTION) {
					            File file = loadFC.getSelectedFile();
					            //This is where a real application would open the file.
					            father.showInfo("Otwieram: "+file.getName()+"\n");
					            father.pushEvent(new EventLoadMap(file.getPath()));
					        }
						}
					}
				); 		
		options.get("Plik/Zapisz jako").addActionListener(
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						int FCOption = saveFC.showOpenDialog((JMenuItem)e.getSource());
						if (FCOption == JFileChooser.APPROVE_OPTION) {
				            File file = saveFC.getSelectedFile();
				            //This is where a real application would open the file.
				            father.showInfo("Zapisuje do: "+file.getName()+"\n");
				            father.pushEvent(new EventSaveMap(file.getPath()));
				        }
					}
				}
			); 
		options.get("Plik/Nowy").addActionListener(
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						father.pushEvent(new EventNewMap());
					}
				}
			); 
		options.get("Plik/Zamknij").addActionListener(
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						father.pushEvent(new EventExitProgram());
					}
				}
			); 		
		options.get("Pomoc/O programie...").addActionListener(
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						/*TODO Shouldn't that be info from model or controller?*/
						JOptionPane.showMessageDialog(father.getFrame(),
							    "Program Yaht Map Editor (YME)\n" +
							    "Autor: Andrzej 'Yester' Fiedukowicz (fiedukow@gmail.com)\n" +
							    "Politechnika Warszawska wydział Elektoroniki i Technik Informacyjnych\n" +
							    "Zadanie projektowe ramach przedmiotu Programowanie Zdarzeniowe\n" +
							    "Program umożliwia tworzenie mapy do gry Yaht, w postaci wektorowej\n",
							    "O programie...",
							    JOptionPane.INFORMATION_MESSAGE
							    );
					}
				}
			); 		
	}
}

class XMLFilter extends FileFilter
{
    public boolean accept(File f) {	               
    	return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
	}
    public String getDescription()
    {
    	return "Pliki mapy YME *.xml";
    }
}
