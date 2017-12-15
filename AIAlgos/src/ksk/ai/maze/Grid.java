package ksk.ai.maze;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	//Copy constructor
	public Grid(Grid g)
	{
		//Set up the initial, unconnected grid
		this(g.getRows(), g.getColumns());		
		
		//Copy all of the connections from the source Grid
		for (Edge<GridNode> e: g.mEdgeSet)
		{
			List<GridNode> nodes = e.getNodes();
			GridNode node1 = (GridNode)nodes.get(0);
			GridNode node2 = (GridNode)nodes.get(1);
			
			connect(node1.getRow(), node1.getColumn(), node2.getRow(), node2.getColumn());
		}
	}
	
	/**
	 * Constructor:  Create a grid of Nodes that completely fill a rectangular pattern with the
	 * given number of rows and columns.  All Nodes can connect to their neighbours (four
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
	 * Get the number of rows in the Grid
	 * @return  The number of rows
	 */
	public int getRows()
	{
		return mRows;
	}

	/**
	 * Get the number of columns in the Grid
	 * @return  The number of columns
	 */
	public int getColumns()
	{
		return mColumns;
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
	 * Check if two Nodes are beside each other (whether or not they are connected) 
	 * 
	 * @param n1 First Node
	 * @param n2 Second Node
	 * @return True if the nodes exist and are adjacent, otherwise false
	 */
	public boolean isAdjacent(GridNode n1, GridNode n2)
	{
		return isAdjacent(n1.getRow(), n1.getColumn(), n2.getRow(), n2.getColumn());
	}

	/**
	 * Check if two Nodes are beside each other (whether or not they are connected)
	 * 
	 * @param r1 Row number for first node
	 * @param c1 Column number for first node
	 * @param r2 Row number for second node
	 * @param c2 Column number for second node
	 * @return True if the nodes exist and are adjacent, otherwise false
	 */
	public boolean isAdjacent(int r1, int c1, int r2, int c2)
	{
		//Check if the nodes are neighbours.  Total displacement must be exactly 1
		int diff = Math.abs(r2-r1) + Math.abs(c2-c1);

		if ((diff==1)&&(isValidLocation(r1,c1))&&(isValidLocation(r2,c2)))
		{
			return true;
		}
		
		return false;
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
		return connect(new GridNode(r1,c1), new GridNode(r2,c2));
	}

	/**
	 * Overridden connect method from parent class.  This is to ensure that non-adjacent nodes
	 * can never be connected
	 * 
	 * @param n1 First Node
	 * @param n2 Second Node
	 * @return Returns true if a new connection was made. Will return false if either Node doesn't exist, they were already connected, or they're not neighbours.
	 */
	public boolean connect(GridNode n1, GridNode n2)
	{
		if (isAdjacent(n1, n2))
		{			
			return super.connect(n1, n2);
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
	 * Get a list of walls.  Walls are special edges that represented nodes that are NOT connected to each other.
	 * but ARE beside each other.
	 * 
	 * @return A Set of Edge objects representing all non-connected adjacent Nodes.
	 * 
	 */
	public Set<Edge<GridNode>> getWalls()
	{
		Set<Edge<GridNode>> result = new HashSet<Edge<GridNode>>();
		
		for (GridNode n1 : mNodeSet)
		{
			for (GridNode n2 : mNodeSet)
			{
				if ((isAdjacent(n1,n2))&&(!isConnected(n1,n2)))
				{
					result.add(new Edge<GridNode>(n1,n2));
				}
			}
		}
		
		return result;
	}


	/**
	 * Get all of the walls around a specific Node.
	 * @param n The Node whose walls we are to look for
	 * @return A Set containing Edge objects that represent non-connections to adjacent Nodes.
	 */
	public Set<Edge<GridNode>> getWalls(GridNode n)
	{
		Set<Edge<GridNode>> result = new HashSet<Edge<GridNode>>();

		//Loop through all Nodes
		for (GridNode x: mNodeSet)
		{
			//Check if a wall exists between this Node and n
			if ((isAdjacent(n,x))&&(getEdge(n, x) == null))
			{
				result.add(new Edge<GridNode>(n,x));
			}
		}

		return result;
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
	 
	 //Without a fixed-width font, this kind of ASCII drawing is a lost case
	public String toString()
	{
		String result = "";
		
		//Start of Grid
		result += "l";
		for (int c=0; c<mColumns; c++)
		{
			result += "--l";  //Two spaces per column, plus 1
		}
		result +="l\n";
		
		//Draw rows
		for (int r = 0; r<mRows; r++)
		{
			//Start of row
			result += "|";
			
			//Draw columns
			for (int c = 0; c<mColumns-1; c++)
			{
				if (isConnected(r,c,r,c+1))
				{
					result += "OO";
				}
				else
				{
					result += "Xl";
				}
			}
			
			//End of row
			result += " l\n";
			
			//Line between rows
			result += "l";
			
			for (int c = 0; c<mColumns-1; c++)
			{
				if (isConnected(r,c,r+1,c))
				{
					result += "OOl";
				}
				else
				{
					result += "--l";
				}
			}
			result += "|\n";
		}
		
		//End of Grid
		result += "|";
		for (int c=0; c<mColumns; c++)
		{
			result += "--|";  //Two spaces per column, plus 1
		}
		result += "|\n";
		
		return result;
	}
*/
	
	//public int hashCode();

	//public boolean equals();
	 

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
