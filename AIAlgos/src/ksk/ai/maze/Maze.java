package ksk.ai.maze;

import java.util.HashMap;
import java.util.Map;
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
	protected GridNode mStart, mGoal;

	//Maintain a map of visited nodes to numbers (could be used if multiple objects are traversing
	//to remember last visitor, or to count steps, etc)
	protected Map<GridNode, Integer> mVisits;

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

		//Initially no visitors
		mVisits = new HashMap<GridNode, Integer>();

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

	/**
	 * Mark a given node as having been visited
	 * 
	 * @param n  The visited node
	 * @param visitor A number representing the visitor (Or step number, etc.)
	 */
	public void visit(GridNode n, Integer visitor)
	{
		if (visitor==null)
		{
			mVisits.remove(n);
		}
		else
		{
			mVisits.put(n,visitor);
		}
	}

	/** Mark a node as unvisited
	 * 
	 * @param n The node to be marked.
	 */
	public void unVisit(GridNode n)
	{
		visit(n, null);
	}

	/**
	 * Get information on the visited status of this node.
	 * 
	 * @param n  The node to check.
	 * @return  The visited status.  This may be the ID of the last visitor, a step number, etc., depending on the application.
	 */
	public Integer getVisit(GridNode n)
	{
		if (!mVisits.containsKey(n))
		{
			return null;
		}

		return mVisits.get(n);
	}
}
