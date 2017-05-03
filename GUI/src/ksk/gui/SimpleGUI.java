package ksk.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 * Class to create and launch a really basic, single-panel GUI.
 * 
 * Makes sure that components are all created in the EDT and dynamically creates a JPanel of the appropriate
 * runtime class in the new Thready using a SimplePanelFactory
 */

public class SimpleGUI implements Runnable {

	SimpleFrame mFrame;
	SimplePanelFactory mPanelFactory;

	//Keep track of whether or not we've launched the GUI; once we launch, title and size changes
	//should be passed to the actual Swing components
	boolean mIsRunning;

	//Allow the size and title to get configured prior to launching the GUI
	Dimension mSize;
	String mTitle;

	//Constructor
	public SimpleGUI(SimplePanelFactory panelFactory)
	{
		mPanelFactory = panelFactory;

		//For now, the title and size should both be null (default)		
		mSize = null;
		mTitle = null;
		mIsRunning = false;
	}

	//Launch the GUI in a new Thread
	public void launchGUI()
	{
		SwingUtilities.invokeLater(this);
	}

	//Set the window title
	public void setTitle(String title)
	{
		if (mIsRunning)
		{
			mFrame.setTitle(title);
		}
		else
		{
			mTitle = title;
		}
	}

	//Set the window (panel) size
	public void setSize(Dimension size)
	{
		if (mIsRunning)
		{
			mFrame.getPanel().setSize(size);
		}
		else
		{
			mSize = new Dimension(size);
		}
	}

	public void run()
	{
		mIsRunning = true;

		//Create a frame
		mFrame = new SimpleFrame();

		//Add the panel to the frame
		JPanel panel = mPanelFactory.getPanel();
		mFrame.add(panel);

		//Set the title and size if necessary
		if (mTitle != null)
		{
			setTitle(mTitle);
		}

		if (mSize != null)
		{
			setSize(mSize);
		}

		//Pack and display
		mFrame.pack();
		mFrame.setVisible(true);
	}



	//********************************************************************************************
	//All of the methods below this line are called after the GUI is running, and thus must only
	//be called from the Event Dispatch Thread
	//********************************************************************************************

	//Return the JFrame associated with this GUI
	public JFrame getFrame()
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			return mFrame;
		}
		else
		{
			return null;
		}
	}

	//Return the JPanel associated with this GUI
	public JPanel getPanel()
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			return mFrame.getPanel();
		}
		else
		{
			return null;
		}
	}
}
