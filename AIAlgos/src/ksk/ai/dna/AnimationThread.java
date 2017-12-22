/**
 * Auto Generated Java Class.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnimationThread implements Runnable, ActionListener {
  
  /* ADD YOUR CODE HERE */
  JFrame myFrame;
  Timer myTimer;
  
    //This actually creates the program's main window.
  //The contents of that window will be drawn by a separate class
  //It also starts the animation timer
  public void run()
  {
    double FPS = 30;
    
    //This is the actual main window
    myFrame = new JFrame ("Main Window");
    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    myFrame.setPreferredSize(new Dimension(500,500));
    
    //Add a JPanel.  This is what we will actually be drawing on
    //Change this class to change your application
    myFrame.add(new GraphicsExample());
    
    myFrame.pack();
    myFrame.setVisible(true);
    
    //Initialize the timer.  Note that the delay, in seconds is 1/FPS.
    //In milliseconds, it's 1000/FPS.
    myTimer = new Timer((int)(1000/FPS), this);
    
    //Start the timer
    myTimer.start();
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == myTimer)
    {
       myFrame.repaint();
    }
  }
}
