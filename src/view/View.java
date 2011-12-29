package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.EventObject;
import java.util.concurrent.BlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import controller.ViewState;

import model.EditorMap;

public class View {
	ViewState currentState;
	BlockingQueue<EventObject> eventQueue;
	JFrame mainFrame;
	MapPanel mapPanel;
	JPanel leftMenu;	
	JPanel toolbox;
	JPanel attributes;
	JPanel statusBar;
	JTextArea statusText;
	JComboBox objectSelected;
	public View( ViewState state, BlockingQueue<EventObject> events )
	{			
		currentState = state;
		mainFrame 	 = new JFrame("YME :: new map");
		mapPanel  	 = new MapPanel( this );
		leftMenu  	 = new JPanel();
		toolbox  	 = new JPanel();
		attributes 	 = new JPanel();
		statusBar 	 = new JPanel();
		Container cp = mainFrame.getContentPane();
		
		
		mainFrame.setPreferredSize(new Dimension(1024,768));
		
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
		
		
		
		statusText = new JTextArea("Debug window:\n",5,100);		
		statusText.setLineWrap(true);
		JScrollPane statusTextScroll = new JScrollPane(statusText);
		statusTextScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		statusBar.add( statusTextScroll );	
		statusBar.setLayout(new GridLayout(1,1));
		mainFrame.setJMenuBar(menuBar);		
		

		/*mapPanel.setBorder(new TitledBorder("Mapa") );*/		
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
		
		JButton buttonPrev = new JButton("<");
		JButton buttonNext = new JButton(">");
		
		objectSelected = new JComboBox();	
		objectSelected.setFont( new Font("Courier", Font.PLAIN, 12) );
		objectSelected.addItem("Cała mapa");
		objectSelected.addItem("Start łodzi");
		objectSelected.addItem("Polygon   #1");
		objectSelected.addItem("Rectangle #2");
		objectSelected.addItem("Ellipse   #3");
		attributes.add(buttonPrev);
		attributes.add(objectSelected);
		attributes.add(buttonNext);
		attributes.setPreferredSize(new Dimension(300, 400));
		
		
		JLabel textureNameLabel = new JLabel("Tekstura: ");
		JTextField textureName = new JTextField(10);
		JButton textureFind = new JButton("...");
		textureFind.setPreferredSize(new Dimension(30, 18));
		attributes.add(textureNameLabel);
		attributes.add(textureName);
		attributes.add(textureFind);
		
		
		
		
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
	
	public void setCurrentState( ViewState map )
	{
		this.currentState = map;
		showInfo("Zaladowano mape o nazwie: "+currentState.getMap().getMapName()+"\n");
		mapPanel.repaint( );		
	}
	
	public void pushEvent( EventObject event )
	{
		try
		{
			this.eventQueue.put(event);
		}
		catch ( InterruptedException e )
		{
			this.showInfo( e.getMessage() );
		}
	}
	
	ViewState getState()
	{
		return currentState;
	}
}