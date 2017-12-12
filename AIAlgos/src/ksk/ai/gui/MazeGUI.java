package ksk.ai.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import ksk.ai.maze.Grid;
import ksk.ai.maze.GridNode;
import ksk.ai.maze.Maze;

/**
 * Allows the drawing of a Maze in Swing.
 * 
 * @author Kern Lewin
 * @version 0.5
 *
 */

public class MazeGUI extends GridGUI {
	
	Maze mMaze;

	public MazeGUI(Maze m)
	{
		super(m.getGrid());

		mMaze = m;
	}

	public void paint(Graphics g)
	{
		//Get the grid geometry for the maze
		Grid grid = mMaze.getGrid();

		//Calculate width and height of cells
		Dimension size = getSize();

		int rows = grid.getRows();
		int columns = grid.getColumns();
		int width = size.width/columns;
		int height = size.height/rows;

		//Loop through all Nodes
		for (int r = 1; r<=rows; r++)
		{
			for (int c=1; c<=columns; c++)
			{
				GridNode current = new GridNode(r,c);

				//Colour start node in green
				if (mMaze.getStart().equals(current))
				{
					g.setColor(Color.green);
					g.fillRect((c-1)*width, (r-1)*height, width, height);
				}
				//Colour goal node in red
				else if (mMaze.getGoal().equals(current))
				{
					g.setColor(Color.red);
					g.fillRect((c-1)*width, (r-1)*height, width, height);
				}
				//Colour disconnected node in black
				/*else if (grid.getNeighbours(current).isEmpty())
				{
					g.fillRect((c-1)*width, (r-1)*height, width, height);
				}*/

				g.setColor(Color.black);
			}
		}

		//Call the superclass to draw any walls
		super.paint(g);
	}
}
