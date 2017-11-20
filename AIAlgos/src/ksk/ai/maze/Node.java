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
 * or a subtype, but for now, I think we'll just use an ID number.  this can have information encoded in it by
 * subclasses, or, for more complex nodes, each node could be associated to an object using a map
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
	public long getID()
	{
		return mID;
	}
}
