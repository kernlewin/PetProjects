package ksk.ai.maze;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Kern Lewin
 *
 * @version 0.5
 * 
 * Collection of Nodes and Edges
 * 
 * A graph consists of a set of Nodes, each of which may or may not be connected to other Nodes by Edges.
 * Basic Edges may have a weight associated with them, but no direction.
 */
public class Graph<N extends Node> {

	//Set of Nodes and Edges
	Set<N> mNodeSet;
	Set<Edge> mEdgeSet;

	/**
	 * Constructor:  Create a graph with Nodes and Edges
	 */
	public Graph(Collection<N> nodes, Collection<Edge> edges)
	{
		mNodeSet = new HashSet<N>();
		mEdgeSet = new HashSet<Edge>();

		//Add all nodes to the graph
		if (nodes != null)
		{
			mNodeSet.addAll(nodes);
		}

		//Add edges to the graph, but ONLY those edges that connect Nodes that are in the Graph
		//Not all Nodes need Edges, but all Edges need Nodes
		if (edges != null)
		{
			mEdgeSet.addAll(edges);
			prune();
		}

	}

	/**
	 * Constructor:  Create an empty graph
	 */
	public Graph()
	{
		this(null, null);
	}

	/**
	 * Add a Node to the graph.  Note that duplicate Nodes will be ignored
	 * 
	 * @param n The Node to include in the Graph
	 * 
	 * @return true if the Node was added to the Graph, false for duplicate or null Nodes
	 */
	public boolean addNode(N n)
	{
		//No Null Nodes
		if (n != null)
		{
			return mNodeSet.add(n);
		}

		return false;
	}

	/**
	 * Remove a Node from the graph, if it exists. Any edges that connected to that Node will also be removed
	 * 
	 * @param n THe Node to be removed
	 * 
	 * @return true if the Node was removed, false if the Node was null, or not in the Graph
	 */
	public boolean removeNode(N n)
	{
		if (n != null)
		{
			boolean returnValue = mNodeSet.remove(n);
			prune();

			return returnValue;
		}

		return false;
	}

	/**
	 * Connect two nodes together with a specified weight.  If they are not already in the Graph, they will be added
	 * 
	 * @param n1 First Node to connect
	 * @param n2 Second Node to connect
	 * @param w Connection weight 
	 */
	public boolean connect(N n1, N n2, double weight)
	{
		//No Edges to Null Nodes (shouldn't be any Null Nodes, but if I add this Edge I'll have
		//to prune it right away)
		if ((n1!=null)&&(n2 != null))
		{
			mNodeSet.add(n1);
			mNodeSet.add(n2);
			return mEdgeSet.add(new Edge(n1, n2, weight));
		}

		return false;
	}

	/**
	 * Connect two nodes together with default weight.  If they are not already in the Graph, they will be added
	 * 
	 * @param n1 First Node to connect
	 * @param n2 Second Node to connect
	 * @param w Connection weight 
	 */
	public boolean connect(N n1, N n2)
	{
		//No Edges to Null Nodes (shouldn't be any Null Nodes, but if I add this Edge I'll have
		//to prune it right away)
		if ((n1!=null)&&(n2 != null))
		{
			mNodeSet.add(n1);
			mNodeSet.add(n2);
			return mEdgeSet.add(new Edge(n1, n2));
		}

		return false;
	}


	/**
	 * Attempt to disconnect (remove the Edge between) two Nodes.  Return true if an Edge was removed,
	 * False if the Edge isn't in the Graph
	 */
	public boolean disconnect(Node n1, Node n2)
	{
		return mEdgeSet.remove(new Edge(n1, n2));
	}

	/**
	 * Get the Edge (if any) connecting the two specified Nodes
	 */
	public Edge getEdge(Node n1, Node n2)
	{
		Edge tempEdge = new Edge(n1, n2);

		for (Edge e: mEdgeSet)
		{
			if (e.equals(tempEdge))
			{
				return e;
			}
		}

		return null;
	}

	/**
	 * Check if two Nodes are connected
	 */
	public boolean isConnected(Node n1, Node n2)
	{
		return (getEdge(n1,n2)!=null);
	}

	/**
	 * Check whether or not a given node is in the Graph
	 * 
	 * @param n Node to search for
	 * 
	 * @returns true if the Node is in the Graph, otherwise false
	 */
	public boolean contains(Node n)
	{
		return mNodeSet.contains(n);
	}


	//Prune away useless Edges.  This method removes any Edges that connect to non-existent Nodes,
	//as well as Edges with a weight of zero.
	protected void prune()
	{
		for (Edge e : mEdgeSet)
		{
			if (e.getWeight()==0)
			{
				mEdgeSet.remove(e);
			}
			else
			{
				Node[] n = e.getNodes();

				//Edge must connect two non-null nodes that are in this Graph
				if ((n == null)||(n[0] == null)||(n[1] == null)||(!mNodeSet.contains(n[0]))||(!mNodeSet.contains(n[1])))
				{
					//Add the edge
					mEdgeSet.remove(e);
				}
			}
		}
	}
}
