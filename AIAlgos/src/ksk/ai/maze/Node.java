package ksk.ai.maze;

/**
 * 
 * @author Kern Lewin
 * 
 * @version 0.5
 *
 * Represents a node in a graph.
 * 
 * Considered having nodes contain objects, either as members, or as a parameterized type,
 * or a subtype, but for now, I think we'll just use an ID number.  Different types of Graphs
 * can use subclasses of Node if they need to store more information.
 */
public class Node {

	//Store our ID number
	private long mID;
	
	/**
	 * Constructor:  Create a new Node.
	 * 
	 * Generate a new Node with a specific Node ID.  The constructing class (typically Graph) will be responsible for
	 * ensuring that these IDs are unique and meaningful within a group of related Nodes.
	 * 
	 * @param id Numeric identifier for this Node.  Typically unique within a Graph, but that is not enforced by this class.
	 */
	public Node(long id)
	{
		mID = id;
	}
	
	/**
	 * Get the ID number for this node
	 */
	public final long getID()
	{
		return mID;
	}
	
	public String toString()
	{
		return String.valueOf(getID());
	}
	
	public boolean equals (Object o)
	{
		if (!(o instanceof Node))
		{
			return false;
		}
		
		if (((Node)o).getID()!= getID())
		{
			return false;
		}
		
		return true;
	}
	
	public int hashCode()
	{
		return (int)(getID()%Integer.MAX_VALUE);
	}
}
