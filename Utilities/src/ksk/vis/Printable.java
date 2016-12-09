package ksk.vis;

import java.awt.Graphics;
import java.io.PrintStream;

//Generic interface for any object that has a visual representation

//This allows us to require that such object support both a console and Graphical representation


public interface Printable {

	//Output the object to a text stream (like standard I/O, or a text file)
	public void drawS(PrintStream s);
	
	//Output the object to a graphics context
	public void drawG(Graphics g);
}
