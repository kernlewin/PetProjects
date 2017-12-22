/**
 * Auto Generated Java Class.
 */

import java.awt.*;
import javax.swing.*;

public class GraphicsExample extends JPanel {
  
  /* ADD YOUR CODE HERE */
  int[] x;
  int[] y;
  
  //This is a Constructor.  It is a special method used to initialize a Java object.
  //Notice that the name is the same as the program name, and there is no return type
  public GraphicsExample()
  {
    //Allocate enough space in x and y for 10 numbers
    x = new int[10];
    y = new int[10];
    
    for (int i=0; i<x.length; i++)
    {
    x[i] = 30*i;
    y[i] = 0;
    }
  }
    
  public void paint(Graphics g)
  {   
    //Get the size of the window
    Rectangle size = g.getClipBounds();
    
    //Draw lines diagonally across the window.
    //Note that the "size" has two parts to it; width and height
    //The drawLine command looks like this:
    //  g.drawLine( x1, y1, x2, y2 );
    g.drawLine(0,0,size.width, size.height);
    g.drawLine(size.width, 0, 0, size.height);
    
    //We can change to a common colour, using one of Java's pre-set Colour constants
    g.setColor(Color.red);
    
    //To set a wider range of colours, we need to specify the amount of red, green and blue
    //in the colour
    Color purple = new Color(153,0,204);
    
    //Now, tell Java to use this new colour we've defined
    g.setColor(purple);
    
    //Update coordinates of circle
    for (int i=0; i<x.length; i++)
    {
      x[i]++;
      y[i]++;
      g.fillOval(x[i],y[i],20,20);
    }
    
    //Font f = new Font("Broadway", Font.BOLD, 60);
    //g.setFont(f);
    
    //g.drawString("Hello World",100,100);

  }
}
