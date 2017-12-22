package ksk.ai.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import ksk.ai.maze.Grid;
import ksk.ai.maze.GridNode;
import ksk.ai.maze.Maze;
import ksk.ai.maze.MazeEvent;
import ksk.ai.util.KEventListener;

/**
 * Allows the drawing of a Maze in Swing.
 * 
 * @author Kern Lewin
 * @version 0.5
 *
 */

public class MazeGUI extends GridGUI implements KEventListener<MazeEvent> {

	//The maze that we are representing
	Maze mMaze;

	//Colors to represent key cells in the maze
	private static final Color START_COLOR = Color.green;
	private static final Color GOAL_COLOR = Color.red;
	private static final Color CLOSED_COLOR = Color.black;
	private static final Color VISITED_COLOR = Color.yellow;

	public MazeGUI(Maze m)
	{
		super(m.getGrid());

		mMaze = m;
		mMaze.getEventBroadcaster().addListener(this);
	}

	@Override
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

				//Color start node in green
				if (mMaze.getStart().equals(current))
				{
					g.setColor(START_COLOR);
					g.fillRect((c-1)*width, (r-1)*height, width, height);
				}
				//Color goal node in red
				else if (mMaze.getGoal().equals(current))
				{
					g.setColor(GOAL_COLOR);
					g.fillRect((c-1)*width, (r-1)*height, width, height);
				}
				//Color visited nodes
				else if (mMaze.getVisit(current) != null)
				{
					g.setColor(VISITED_COLOR);
					g.fillRect((c-1)*width, (r-1)*height, width, height);					
				}
								//Color disconnected node in black
				else if (grid.getNeighbours(current).isEmpty())
				{
					g.setColor(CLOSED_COLOR);
					g.fillRect((c-1)*width, (r-1)*height, width, height);
				}

				g.setColor(Color.black);
			}
		}

		//Call the superclass to draw any walls
		super.paint(g);
	}

	@Override
	public void onEvent(MazeEvent event) {
		// TODO Auto-generated method stub
		repaint();
	}
}
