package ksk.ai.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import ksk.ai.maze.Grid;

/**
 * 
 * @author Kern Lewin
 * 
 * @version 0.5
 *
 * Graphical interface for a ksk.ai.maze.Grid
 */

public class GridGUI extends JPanel {
	//What proportion of  the gap between adjacent open cells should the wall occupy
	private static final double OPEN_WALL_SIZE = 0.0;
	
	//The Grid object that we will visualize
	Grid mGrid;

	/**
	 * Create a GridGUI with attached Grid.	
	 */
	public GridGUI(Grid g)
	{
		mGrid = g;
	}

	//Paint the GUI component
	public void paint(Graphics g)
	{
		Dimension size = getSize();

		int rows = mGrid.getRows();
		int columns = mGrid.getColumns();
		int cellWidth = size.width / columns;
		int cellHeight = size.height / rows;
		int wallHeight = (int)(cellHeight * OPEN_WALL_SIZE);
		int wallWidth = (int)(cellWidth * OPEN_WALL_SIZE);

		int x = 0;
		int y=0;
		for (int r = 1; r<=rows; r++)
		{
			for (int c = 1; c<=columns; c++)
			{
				//Left wall
				if (mGrid.isConnected(r, c, r, c-1))
				{
					g.drawLine(x, y, x, y + wallHeight);
					g.drawLine(x, y + cellHeight - wallHeight, x, y + cellHeight);
				}
				else
				{
					g.drawLine(x, y, x, y + cellHeight);
				}

				//Right wall
				x += cellWidth-1;
				if (mGrid.isConnected(r, c, r, c+1))
				{
					g.drawLine(x, y, x, y + wallHeight);
					g.drawLine(x, y + cellHeight - wallHeight, x, y + cellHeight);
				}
				else
				{
					g.drawLine(x, y, x, y + cellHeight);
				}
				x -= cellWidth-1;


				//Top wall
				if (mGrid.isConnected(r, c, r-1, c))
				{
					g.drawLine(x,y,x + wallWidth,y);
					g.drawLine(x + cellWidth - wallWidth,y,x + cellWidth,y);
				}
				else
				{
					g.drawLine(x,y,x + cellWidth,y);
				}

				//Bottom wall
				y += cellHeight-1;
				if (mGrid.isConnected(r, c, r+1, c))
				{
					g.drawLine(x,y,x + wallWidth,y);
					g.drawLine(x + cellWidth - wallWidth,y,x + cellWidth,y);
				}
				else
				{
					g.drawLine(x,y,x + cellWidth,y);
				}
				y -= cellHeight - 1;
				
				//Next Column
				x += cellWidth;
			}

			//Next Row
			x = 0;
			y += cellHeight;
		}
	}
}
