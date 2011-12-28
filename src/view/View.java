package view;

import javax.swing.JFrame;

public class View {
	JFrame mainFrame;
	public View()
	{
		mainFrame = new JFrame("YME :: new map");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        System.out.println("View created.");        
	}
}
