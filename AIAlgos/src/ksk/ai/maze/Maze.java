package ksk.ai.maze;

import java.util.Random;

/**
 * 
 * @author Kern Lewin
 * @version 0.5
 * 
 * This class represents a maze  A maze contains a grid in which some squares are connected to their
 * neighbours, and some are not.  A maze contains at least two special Nodes; a start node and 
 * a goal node.  There is guaranteed to be a path between these two nodes.
 * 
 * Note that Mazes do not extend Grids because the structure of a maze is immutable; once created
 * nobody should be able to connect/disconnect nodes (some maze subclasses may modify this behaviour)
 */

public class Maze {
	
	//The grid that represents all of the nodes and connections of the maze
	protected Grid mGrid;
	GridNode mStart, mGoal;
	
	/**
	 * Contructor:  Create a maze of the given dimensions.
	 * 
	 * @param rows  Number of rows in the maze
	 * @param columns Number of columns in the maze
	 */
	public Maze(int rows, int columns)
	{
		this (rows, columns, null);
	}
	
	/**
	 * Constructor:  Create a maze of the given dimensions, using a specific maze-generation
	 * algorithm
	 * @param rows  Number of rows in the maze
	 * @param columns Number of columns in the maze
	 * @param gen  Maze-generating algorithm to use
	 */
	public Maze(int rows, int columns, MazeGenerator gen)
	{
		//Create grid
		mGrid = new Grid(rows, columns);
		
		//Choose start and goal locations
		Random r = new Random();
		int startRow = r.nextInt(rows)+1;
		int startColumn = r.nextInt(columns)+1;
		int goalRow = r.nextInt(rows)+1;
		int goalColumn = r.nextInt(columns)+1;
		
		mStart = new GridNode(startRow, startColumn);
		mGoal = new GridNode(goalRow, goalColumn);
		
		//Generate the maze, with (at least) a path from start to goal
		if (gen != null)
		{
			gen.generate(mGrid, mStart, mGoal);
		}
		else
		{
			//default
			mGrid.connectAll();
		}
	}
	
	/**
	 * Get the Start location for this maze
	 * 
	 * @return  A GridNode representing the maze's starting location
	 */
	public GridNode getStart()
	{
		return mStart;
	}
	
	/**
	 * Get the Goal location for this maze
	 * 
	 * @return  A GridNode representing the maze's goal location
	 */
	public GridNode getGoal()
	{
		return mGoal;
	}
	
	/**
	 * Get a copy of the entire Grid for this maze.  Note that we must NOT expose the actual
	 * Grid, or anybody can arbitrarily modify the structure of the maze
	 * 
	 * @return  A deep copy of the Grid for this maze.
	 */
	public Grid getGrid()
	{
		return new Grid(mGrid);
	}
	
	
}
