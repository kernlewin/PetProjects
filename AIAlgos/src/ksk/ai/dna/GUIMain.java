/**
 * Auto Generated Java Class.
 */

/*
 * This class is designed to launch a graphical Java program.
 * We can re-use it for all of the programs we do that use graphics.
 * All of the actual drawing will happen in a separate class
 */

//Import standard Java libraries and Swing
import java.awt.*;
import javax.swing.*;

public class GUIMain {
  
  
  public static void main(String[] args) { 
    
    //This command tells Java to load and run the GUI in a separate thread
    javax.swing.SwingUtilities.invokeLater( new AnimationThread());
    
  }
}
