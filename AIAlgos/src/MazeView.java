import java.awt.Dimension;

import javax.swing.JFrame;

import ksk.ai.gui.GridGUI;
import ksk.ai.gui.MazeGUI;
import ksk.ai.maze.Grid;
import ksk.ai.maze.KruskalMazeGen;
import ksk.ai.maze.Maze;


public class MazeView {

	/**
	 * @param args
	 */
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				createGUI();
			}
		});
		

	}
	
	public static void createGUI()
	{
		/*
		Grid grid = new Grid(5,5);
		grid.connectAll();
		GridGUI gui = new GridGUI(grid);
		gui.setPreferredSize(new Dimension(500,500));
		*/
		
		Maze maze = new Maze(20,20, new KruskalMazeGen());
		MazeGUI gui = new MazeGUI(maze);
		gui.setPreferredSize(new Dimension(500,500));
	
		JFrame frame = new JFrame("MazeView");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(gui);
		frame.pack();
		frame.setVisible(true);
	}

}
