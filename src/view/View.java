package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

public class View {
	JFrame mainFrame;
	JPanel mapPanel;
	JPanel leftMenu;	
	JPanel toolbox;
	JPanel attributes;
	JPanel statusBar;
	JTextArea statusText;
	public View()
	{		
		mainFrame 	 = new JFrame("YME :: new map");
		mapPanel  	 = new JPanel();
		leftMenu  	 = new JPanel();
		toolbox  	 = new JPanel();
		attributes 	 = new JPanel();
		statusBar 	 = new JPanel();
		Container cp = mainFrame.getContentPane();
		
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Plik");
		menuBar.add(menu);

		JMenuItem item = new JMenuItem("Nowy");
		menu.add(item);
		item = new JMenuItem("Otwórz");
		menu.add(item);
		item = new JMenuItem("Zapisz jako..");
		menu.add(item);
		item = new JMenuItem("Zamknij");
		menu.add(item);
		
		menu = new JMenu("Pomoc");
		menuBar.add(menu);
		item = new JMenuItem("O programie...");
		menu.add(item);
		item = new JMenuItem("Podręcznik użytkownika");
		menu.add(item);
		
		
		
		statusText = new JTextArea(5,100);		
		statusText.setLineWrap(true);
		JScrollPane statusTextScroll = new JScrollPane(statusText);
		statusTextScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		statusBar.add( statusTextScroll );	
		statusBar.setLayout(new GridLayout(1,1));
		mainFrame.setJMenuBar(menuBar);		
		

		mapPanel.setBorder(new TitledBorder("Mapa") );		
		toolbox.setBorder(new TitledBorder("Narzedzia") );
		attributes.setBorder(new TitledBorder("Wlasciwosci"));
		
		
		leftMenu.setLayout(new BorderLayout());
		leftMenu.add(toolbox, BorderLayout.NORTH);
		leftMenu.add(attributes, BorderLayout.CENTER);
		
		toolbox.setLayout(new GridLayout(2,4));
		toolbox.setPreferredSize( new Dimension(192,128) );
		
		JButton ButtonChooser = new JButton(new ImageIcon("icons/arrow.png"));
		ButtonChooser.setToolTipText("Zaznacz obiekt na mapie");
		JButton ButtonStartPoint = new JButton(new ImageIcon("icons/compass.png"));
		ButtonStartPoint.setToolTipText("Zaznacz punkt startowy lodzi");
		JButton ButtonPolygon = new JButton(new ImageIcon("icons/polygon.png"));
		ButtonPolygon.setToolTipText("Rysuj dowolny poligon");
		JButton ButtonRectangle = new JButton(new ImageIcon("icons/rectangle.png"));
		ButtonRectangle.setToolTipText("Rysuj prostokat");
		JButton ButtonEllipse = new JButton(new ImageIcon("icons/ellipse.png"));
		ButtonEllipse.setToolTipText("Rysuj elipse");
		JButton ButtonQuey = new JButton(new ImageIcon("icons/quey.png"));
		ButtonQuey.setToolTipText("Rysuj keje");
		
		
		
		toolbox.add(ButtonChooser);
		toolbox.add(ButtonStartPoint);
		toolbox.add(ButtonPolygon);		
		toolbox.add(ButtonRectangle);
		toolbox.add(ButtonEllipse);
		toolbox.add(ButtonQuey);
		
		
		//attributes.setLayout(new GridLayout(2,2));
		attributes.setPreferredSize( new Dimension(192,512) );
		attributes.add(new JButton("K"));
		attributes.add(new JButton("K"));
		attributes.add(new JButton("Kotek"));
		attributes.add(new JButton("Kotek"));
		
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.repaint();
        cp.setLayout(new BorderLayout());
        cp.add(mapPanel, BorderLayout.CENTER);
        cp.add(leftMenu, BorderLayout.EAST);
        cp.add(statusBar, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
        System.out.println("View created.");      
	}
	
	public void showInfo( String toShow )
	{
		statusText.append(toShow);
		statusText.setCaretPosition( statusText.getText().length() );
	}
}