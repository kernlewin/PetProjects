package ksk.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * The simple frame is the frame of our very basic GUI Framework
 * It is intended to contain a single JPanel, which it gets dynamically from a SimplePanelFactory when the GUI
 * is being launched (not at construction time).
 */

public class SimpleFrame extends JFrame {

	//Use a static counter to generate unique window titles
	static int sFrameCount = 0;

	//Constructor
	public SimpleFrame()
	{
		//Call JFrame constructor
		super();

		//Set the title to default
		setTitle("SimpleFrame" + (++sFrameCount));
	}

	//Set title
	public void setTitle(String title)
	{
		if (title != null)
		{
			setTitle(title);
		}
	}

	//Change panel
	public void changePanel(JPanel newPanel)
	{
		if (newPanel != null)
		{
			newPanel.setSize(getSize());
			setContentPane(newPanel);
		}
	}
	
	//Get the content pane
	public JPanel getPanel()
	{
		return (JPanel)getContentPane();
	}
}
