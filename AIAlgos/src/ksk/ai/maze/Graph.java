package ksk.ai.maze;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
	Set<Edge<N>> mEdgeSet;

	/**
	 * Constructor:  Create a graph with Nodes and Edges
	 */
	public Graph(Collection<N> nodes, Collection<Edge<N>> edges)
	{
		mNodeSet = new HashSet<N>();
		mEdgeSet = new HashSet<Edge<N>>();

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
			return mEdgeSet.add(new Edge<N>(n1, n2, weight));
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
			return mEdgeSet.add(new Edge<N>(n1, n2));
		}

		return false;
	}


	/**
	 * Attempt to disconnect (remove the Edge between) two Nodes.  Return true if an Edge was removed,
	 * False if the Edge isn't in the Graph
	 */
	public boolean disconnect(N n1, N n2)
	{
		return mEdgeSet.remove(new Edge<N>(n1, n2));
	}

	/**
	 * Get the Edge (if any) connecting the two specified Nodes
	 */
	public Edge<N> getEdge(N n1, N n2)
	{
		Edge<N> tempEdge = new Edge<N>(n1, n2);

		for (Edge<N> e: mEdgeSet)
		{
			if (e.equals(tempEdge))
			{
				return e;
			}
		}

		return null;
	}

	/**
	 * Get all Edges (if any) connected to a particular Node
	 * 
	 * @param n This is the Node whose Edges we are to search for
	 * @return A Set of Edges.  This Set may be empty (if the Node is not connected, or doesn't exist) but will not be null
	 */
	public Set<Edge<N>> getEdges(N n)
	{
		Set<Edge<N>> result = new HashSet<Edge<N>>();

		//Loop through all edges
		for (Edge<N> e: mEdgeSet)
		{
			List<N> nodes = e.getNodes();

			//Check if n is one of the Nodes connected to this Edge
			if (nodes.contains(n))
			{
				result.add(e);
			}
		}

		return result;
	}

	/**
	 * Get all of the Nodes connected to a particular Node
	 * @param n The Node whose neighbours we are to look for
	 * @return A Set containing all the Nodes that share an Edge with Node n.  This Set may be empty, but will not be null.  Note that n will be considered its own neighbour if it is connected to itself.
	 */
	public Set<N> getNeighbours(N n)
	{
		Set<N> result = new HashSet<N>();

		//Loop through all Nodes
		for (Edge<N> e: mEdgeSet)
		{
			//Get the nodes on this edge
			List<N> nodes = e.getNodes();
			
			//If one of the nodes is n, add the other to the neighbour list
			int index = nodes.indexOf(n);
			if (index >= 0)
			{
				index = (index + 1)%2;
				result.add( nodes.get(index));
			}
		}

		return result;
	}

	/**
	 * Check if two Nodes are connected
	 */
	public boolean isConnected(N n1, N n2)
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

	/**
	 * Get all of the Nodes in the graph. This will be a shallow copy of the internal set of Nodes
	 * (Nodes are immutable, Sets are not)
	 * 
	 * @return  A Set of all of the Nodes in the Graph
	 */
	public Set<N> getNodes()
	{
		return new HashSet<N>(mNodeSet);
	}
	
	/**
	 * Get all of the Edges in the graph. This will be a shallow copy of the internal set of Edges
	 * (Edges are immutable, Sets are not)
	 * 
	 * @return  A Set of all of the Edges in the Graph
	 */
	public Set<Edge<N>> getEdges()
	{
		return new HashSet<Edge<N>>(mEdgeSet);
	}
	
	//Prune away useless Edges.  This method removes any Edges that connect to non-existent Nodes,
	//as well as Edges with a weight of zero.
	protected void prune()
	{
		for (Edge<N> e : mEdgeSet)
		{
			if (e.getWeight()==0)
			{
				mEdgeSet.remove(e);
			}
			else
			{
				List<N> n = e.getNodes();

				//Edge must connect two non-null nodes that are in this Graph
				if ((n == null)||(n.get(0) == null)||(n.get(1) == null)||(!mNodeSet.contains(n.get(0)))||(!mNodeSet.contains(n.get(1))))
				{
					//Add the edge
					mEdgeSet.remove(e);
				}
			}
		}
	}
}
