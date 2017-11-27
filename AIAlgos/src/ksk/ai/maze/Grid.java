package ksk.ai.maze;

/**
 * 
 * @author Kern Lewin
 * 
 * @version 0.5
 * 
 * A Grid is a Rectangular Graph.  Nodes in the Grid are denoted by a row and column number.
 * Rows and Columns need not be full, and they need not be connected to all of their neighbours.
 * When constructed, the Nodes in a Grid are not connected
 * 
 * Connections are either open or closed; no other "weights" are allowed
 */
public class Grid extends Graph<GridNode>{

	//Keep track of the range of rows and columns
	int mRows, mColumns;

	/**
	 * Constructor:  Create a grid of Nodes that completely fill a rectangular pattern with the
	 * given number of rows and columns.  All Nodes are connected to their neighbours (four
	 * connections for interior nodes, 3 for sides, two for corners)
	 * 
	 * @param rows  Number of rows in the Grid
	 * @param columns Number of columns in the Grid
	 */
	public Grid(int rows, int columns)
	{
		super();

		//Set the min/max rows and columns
		//Note that neither parameter may be negative.  Negative parameters will be treated as zero.
		//Row/Column numbers start at 1, not zero.
		mRows = Math.max(0, rows);
		mColumns = Math.max(0, columns);

		//Add all of the rows and columns to the Grid
		for (int r = 1; r<=mRows; r++)
		{
			for (int c = 1; c <= mColumns; c++)
			{
				addNode(new GridNode(r, c));
			}
		}
	}

	/**
	 * Create an initially-empty Grid.  Nodes will presumably be added and connected later on.
	 */
	public Grid()
	{
		super();

		mRows = 0;
		mColumns = 0;
	}

	/**
	 * Check if two Nodes are connected to each other by an Edge.
	 * 
	 * @param r1 Row number for first node
	 * @param c1 Column number for first node
	 * @param r2 Row number for second node
	 * @param c2 Column number for second node
	 * @return True if the nodes exist and are connected, otherwise false
	 */
	public boolean isConnected (int r1, int c1, int r2, int c2)
	{
		return isConnected(new GridNode(r1,c1), new GridNode(r2,c2));
	}

	/**
	 * Connect a Node to a neighbour.  Returns true if a new connection was made.
	 * Will return false if either Node doesn't exist, they were already connected, or
	 * they're not neighbours.
	 * 
	 * @param r1 Row number for first node
	 * @param c1 Column number for first node
	 * @param r2 Row number for second node
	 * @param c2 Column number for second node
	 * 
	 * @return Returns true if a new connection was made. Will return false if either Node doesn't exist, they were already connected, or they're not neighbours.
	 */
	public boolean connect(int r1, int c1, int r2, int c2)
	{
		//Check if the nodes are neighbours.  Total displacement must be exactly 1
		int diff = Math.abs(r2-r1) + Math.abs(c2-c1);

		if ((diff==1)&&(isValidLocation(r1,c1))&&(isValidLocation(r2,c2)))
		{			
			return connect(new GridNode(r1,c1), new GridNode(r2,c2));
		}

		return false;
	}

	/**
	 * Connect a Node to all of its neighbours
	 * @param r  Row of Node to connect
	 * @param c  Column of Node to connect
	 * 
	 * @return Number of new connections made
	 */
	public int connect(int r, int c)
	{
		int connections = 0;

		//Try all four neighbours
		if (connect(r,c,r+1,c))
		{
			connections++;
		}

		if (connect(r,c,r-1,c))
		{
			connections++;
		}

		if (connect(r,c,r,c+1))
		{
			connections++;
		}

		if (connect(r,c,r,c-1))
		{
			connections++;
		}

		return connections;
	}


	/**
	 * Connect all Nodes to all neighbours (fully connect the Grid)
	 */
	public void connectAll()
	{
		for (GridNode node : mNodeSet)
		{
			connect(node.getRow(), node.getColumn());
		}
	}

	/**
	 * 
	 * Disconnect two Nodes from each other.
	 * 
	 * @param r1 Row number for first node
	 * @param c1 Column number for first node
	 * @param r2 Row number for second node
	 * @param c2 Column number for second node
	 * 
	 * @return  True if an Edge was found and removed, otherwise false.
	 */
	public boolean disconnect(int r1, int c1, int r2, int c2)
	{
		return disconnect(new GridNode(r1,c1), new GridNode(r2,c2));
	}

	/**
	 * Disconnect a Node from all of its neighbours
	 * 
	 * @param r  Row number of Node to disconnect
	 * @param c  Column number of Node to disconnect
	 * 
	 * @return  The number of removed connections
	 */
	public int disconnect(int r, int c)
	{
		int removed = 0;

		//Try all four neighbours
		if (disconnect(r,c,r+1,c))
		{
			removed++;
		}

		if (disconnect(r,c,r-1,c))
		{
			removed++;
		}

		if (disconnect(r,c,r,c+1))
		{
			removed++;
		}

		if (disconnect(r,c,r,c-1))
		{
			removed++;
		}

		return removed;
	}

	/**
	 * Clear all Edges from the graph
	 */
	public void disconnectAll()
	{
		mEdgeSet.clear();
	}

	/**
	 * Clear any Nodes, that are outside of the Grid area, as well as any Edges for
	 * non-existent Nodes
	 */
	protected void prune()
	{
		for (GridNode n : mNodeSet)
		{
			if (!isValidLocation( n.getRow(), n.getColumn()))
			{
				removeNode(n);
			}
		}
		
		prune();
	}

	/*
	public String toString();

	public int hashCode();

	public boolean equals();
*/
	
	//Check if this is a location within the bounds of the Grid (Node may not actually exist)
	private boolean isValidLocation(int r, int c)
	{
		if ((r > 0) && ( r <= mRows)&&
				(c>0)&&(c <= mColumns))
		{
			return true;
		}

		return false;
	}
}
