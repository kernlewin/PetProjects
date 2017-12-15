package ksk.ai.maze;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Kern Lewin
 *
 * @version 0.5
 * 
 * Represents a connection between two Nodes
 * 
 * An Edge connects two Nodes together with some associated domain-specific weighting factor.
 * Default weight is 1.0 
 */
public class Edge<N extends Node> {

	//Class default weight
	private static double DEFAULT_WEIGHT = 1.0;
	
	//Edge weight
	private double mWeight;
	
	//Edge Nodes
	private N mNode1, mNode2;
	/**
	 * Constructor: Create a new Edge with specified weight
	 */
	public Edge(N n1, N n2, double weight)
	{
		mNode1 = n1;
		mNode2 = n2;
		mWeight = weight;
	}
	
	/**
	 * Constructor:  Create a new Edge with default weight
	 */
	public Edge(N n1, N n2)
	{
		this(n1, n2, DEFAULT_WEIGHT);
	}
	
	/**
	 * Get the weight of this Edge
	 */
	public double getWeight()
	{
		return mWeight;
	}
	
	/**
	 * Change the weight of this Edge
	 */
	public void setWeight(double w)
	{
		mWeight = w;
	}
	
	/**
	 * Get the two Nodes connected by this edge.  
	 */
	public List<N> getNodes()
	{
		List<N> result =  new ArrayList<N>();
		result.add(mNode1);
		result.add(mNode2);
		
		return result;
	}
	
	public String toString()
	{
		return mNode1.toString() + "<->" + mNode2.toString();
	}
	
	public int hashCode()
	{
		//Combine the Node hashCodes
		return (mNode1.hashCode()+mNode2.hashCode())%Integer.MAX_VALUE;
	}
	
	public boolean equals(Object o)
	{
		if (!(o instanceof Edge))
		{
			return false;
		}
		
		List<N> nodes = ((Edge<N>)o).getNodes();
		
		if ( nodes.contains(mNode1) && nodes.contains(mNode2))
		{
			return true;
		}
		
		return false;
	}
}
