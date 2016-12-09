package ksk.AI.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
 * Represents a generic Graph.  A graph is just a set of Nodes and a set of Edges
 * A graph can potentially contain a mixture of directed and undirected edges
 */

//Nodes will, in general, have an object associated with them that represents the value (or state) of that node.
//N is the type of the object associated with each Node
public class Graph<N> {

	//Our set of nodes and edges
	protected Set<Node<N>> m_nodes;
	protected Set<Edge<N>> m_edges;

	//Create an empty Graph
	public Graph()
	{
		this(new ArrayList<Node<N>>());  //Empty list
	}

	//Create a Graph with a set of Nodes
	public Graph(List<Node<N>> nodes)
	{
		m_nodes = new HashSet<Node<N>>();
		m_edges = new HashSet<Edge<N>>();

		//If the list isn't null, add any non-null Nodes in it to the Graph
		if (nodes != null)
		{
			for (Iterator<Node<N>> iter = nodes.iterator(); iter.hasNext(); )
			{
				Node<N> node = iter.next();

				if (node != null)
				{
					m_nodes.add(node);
				}
			}
		}
	}
	
	//Copy Constructor
	public Graph(Graph<N> src)
	{
		//Copy all nodes from the src Graph
		
	}

	//Get the size of the Graph (number of Nodes)
	public int getSize()
	{
		return m_nodes.size();
	}

	//Get a list of all of the Nodes.  We'll make a copy to protect the original list against modification
	public List<Node<N>> getNodes()
	{
		List<Node<N>> result = new ArrayList<Node<N>>();
		result.addAll(m_nodes);

		return result;
	}

	//Add a Node to the Graph; note that it will initially be completely disconnected.  Returns false if the Node could not be added
	public boolean addNode(Node<N> newNode)
	{
		//Do not add a null node!
		if (newNode==null)
		{
			return false;
		}
		
		return m_nodes.add(newNode);
	}

	//Connect two nodes in the Graph.  Note that if the Nodes aren't already in the graph they will be added
	//Note that this class will clean up connections automatically; such that every pair of nodes is either not connected, has a single
	//non-directional connection, a single directional connection, or two distinct directional connections
	public void connect(Node<N> n1, Node<N> n2)
	{
		connect(n1, n2, false, 1.0);
	}

	//Add a non-directional weighted connection
	public void connect(Node<N> n1, Node<N> n2, double weight)
	{
		connect(n1,n2,false, weight);	
	}

	//Connect two nodes in the Graph with a (potentially) directed, weighted Edge
	public void connect(Node<N> n1, Node<N> n2, boolean directional, double weight)
	{
		//Make sure the nodes are in the graph
		m_nodes.add(n1);
		m_nodes.add(n2);

		//Get the edges (if any) connecting these nodes
		List<Edge<N>> edges = getEdges(n1, n2);

		//A non-directed edge overrides all edges
		if (!directional)
		{
			m_edges.removeAll(edges);
			m_edges.add(new Edge<N>(n1, n2, directional, weight));
		}

		else
		{
			//Let's do an inventory of the existing edges.  Note that there can be at most one edge in each direction
			//and one non-directional
			Edge<N> same = null;  //Edge (if any) in the same direction
			Edge<N> opposite = null;  //Edge (if any) in the opposite direction
			Edge<N> nonDirected = null;  //Non-directional edge (if any)

			for (Iterator<Edge<N>> iter = edges.iterator(); iter.hasNext(); )
			{
				Edge<N> e = iter.next();
				List<Node<N>> nodes = e.getNodes();

				if (!(e.isDirected()))
				{
					nonDirected = e; 
				}
				else if (nodes.get(0).equals(n1))
				{
					same = e;
				}
				else
				{
					opposite = e;
				}
			}

			if ((nonDirected != null)&&(opposite != null))  //If there's a non-directed and an opposite edge, remove the non-directed edge
			{
				m_edges.remove(nonDirected);
			}
			else if (nonDirected != null) //If there's a non-directed edge and NO opposite edge, then create an opposite edge (the new edge overrides one direction only)
			{
				opposite = new Edge<N>(n2, n1, true, nonDirected.getWeight());
				m_edges.add(opposite);
				m_edges.remove(nonDirected);
			}

			if (same != null)//Remove any existing edge in the same direction
			{
				m_edges.remove(same);
			}

			//If the opposite edge (either found or created) has the same weight as the new edge, then they combine to make
			//a non-directed edge
			if ((opposite != null)&&(opposite.getWeight() == weight))
			{
				m_edges.remove(opposite);
				m_edges.add(new Edge<N>(n1, n2, false, weight));
			}
			else  //Otherwise, just add the new node
			{
				m_edges.add(new Edge<N>(n1, n2, directional, weight));
			}

		}
	}

	//Disconnect two nodes in the Graph (if they exist, and are connected).
	//Return the number of edges removed
	public int disconnect(Node<N> n1, Node<N> n2)
	{
		//Get a list (possibly empty) of all edges between these nodes
		List<Edge<N>> edgeList = getEdges(n1, n2);
		
		m_edges.removeAll(edgeList);
		
		return edgeList.size();
	}
	
	//Check if a Node is in the Graph
	public boolean nodeExists(Node<N> node)
	{
		return ((node != null)&&( m_nodes.contains(node)));
	}

	//Check if a Node is connected to another Node
	public boolean isConnected(Node<N> n1, Node<N> n2)
	{
		return (getEdges(n1,n2).size() > 0 );
	}
	
	//Check the weight of a connection from n1 to n2; if the Nodes are not in the Graph, or not connected, returns Double.NaN
	public double getWeight(Node<N> n1, Node<N> n2)
	{
		List<Edge<N>> edgeList = getEdges(n1, n2);
		
		for (Edge<N> e : edgeList)
		{
			//Return the weight of any non-directed edge, or edge in the correct direction
			//(there should be only one of these)
			if ((e.getNodes().get(0) == n1)||(!e.isDirected()))
			{
				return e.getWeight();
			}
		}
		
		//No edge found connecting these nodes in this direction
		return Double.NaN;
	}
	
	//Return a new Graph containing only the Nodes connected to a specified root node.
	//All relevant Edges are also kept.
	public Graph<N> getSubGraph(Node<N> root)
	{
		
		//Create a new, empty Graph
		Graph<N> result = new Graph<N>();

		//If the root is not a node in the current graph, return an empty graph
		if (!nodeExists(root))
		{
			return result; 
		}

		//Add the root
		result.addNode(root);
		
		//Loop through all the nodes in the graph
		for (Node<N> n : m_nodes)
		{
			//Get a list of the edges connecting the root to this node
			List<Edge<N>> list = getEdges(root, n);
			
			//If the list is not empty, add this node and all of the edges to the graph
			if (!list.isEmpty())
			{
				result.m_nodes.add(n);
				result.m_edges.addAll(list);
			}
		}
		
		return result;
	}

	//PRIVATE:  Get the actual edge object(s) connecting two nodes.  Nobody outside of this class should need this.
	private List<Edge<N>> getEdges(Node<N> n1, Node<N> n2)
	{
		List<Edge<N>> result = new ArrayList<Edge<N>>();

		//If either node is null, just return the empty list
		if ((n1 == null)||(n2==null))
		{
			return result;
		}
		
		for (Iterator<Edge<N>> iter = m_edges.iterator(); iter.hasNext(); )
		{
			//Check if the nodes in this edge (in either order) match n1 and n2
			Edge<N> edge = iter.next();
			List<Node<N>> nodes = edge.getNodes();
			if ((nodes.contains(n1))&&(nodes.contains(n2)))
			{
				result.add(edge);
			}
		}
		return result;
	}
}
