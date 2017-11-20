package ksk.ai.maze;


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
public class Edge {

	//Class default weight
	private static double DEFAULT_WEIGHT = 1.0;
	
	//Edge weight
	private double mWeight;
	
	//Edge Nodes
	private Node mNode1, mNode2;
	/**
	 * Constructor: Create a new Edge with specified weight
	 */
	public Edge(Node n1, Node n2, double weight)
	{
		mNode1 = n1;
		mNode2 = n2;
		mWeight = weight;
	}
	
	/**
	 * Constructor:  Create a new Edge with default weight
	 */
	public Edge(Node n1, Node n2)
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
	public Node[] getNodes()
	{
		return new Node[]{mNode1, mNode2};
	}
}
