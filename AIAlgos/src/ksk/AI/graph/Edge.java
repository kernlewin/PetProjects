package ksk.AI.graph;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents a connection between two nodes in a graph.
 * Edges may connect a node to itself
 * Edges may (or may not) be directed
 * All edges are weighted, though for some applications this weight will be ignored.  The default weight of an edge is 1.0
 * 
 * Note that T in this parameterized class is the type of the NODES.  Other than the weight, there is really no value
 * associated with the edges
 */

public class Edge<T> {
	boolean m_IsDirected;
	double m_Weight;
	Node<T> m_N1, m_N2;

	//Create an undirected edge with default weight
	public Edge(Node<T> n1, Node<T> n2)
	{
		this(n1, n2, false, 1.0);
	}

	//Create a (possibly) directed edge with default weight
	public Edge(Node<T> n1, Node<T> n2, boolean directed)
	{
		this(n1,n2,directed, 1.0);
	}

	//Create a weighted edge
	//Note that if the edge is directed, it is assumed to go from n1 to n2
	public Edge(Node<T> n1, Node<T> n2, boolean directed, double weight)
	{
		//Only restriction on these parameters is that the nodes can't be null
		if ((n1 == null)||(n2 == null))
		{
			throw new RuntimeException("Edge cannot connect to a NULL Node!");
		}

		m_IsDirected = directed;
		m_N1 = n1;
		m_N2 = n2;
		m_Weight = weight;
	}

	//Get the weight of the edge
	public double getWeight()
	{
		return m_Weight;
	}

	//Change the weight of an edge; doesn't affect direction
	public void setWeight(double weight)
	{
		m_Weight = weight;
	}
	
	//Get the directedness of the edge
	public boolean isDirected()
	{
		return m_IsDirected;
	}

	//Get the Nodes this edge connects to
	//Note that the list returned is guaranteed to have a length of exactly 2
	public List<Node<T>> getNodes()
	{
		List<Node<T>> result = new ArrayList<Node<T>>();
		result.add(m_N1);
		result.add(m_N2);

		return result;
	}

	//Need to override equal and hash methods
	public boolean equals(Object obj)
	{
		if (obj instanceof Edge)
		{
			Edge<T> e = (Edge<T>)obj;

			if ((e.m_N1.equals(m_N1))&&(e.m_N2.equals(m_N2)))
			{
				return true;
			}
			else if ((e.m_N1.equals(m_N2))&&(e.m_N2.equals(m_N1)))
			{
				return true;
			}
		}

		//obj wasn't an Edge, or the edges didn't match
		return false;
	}

	public int hashCode()
	{
		return m_N1.hashCode() + m_N2.hashCode();
	}

}
