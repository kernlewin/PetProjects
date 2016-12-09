package ksk.AI.graph;

/*
 * Each Node in a graph must have an object associated with it that represents the value (or state, or some other property)
 * of the Node.  T is the type of that object
 */

public class Node<T> {

	//The Node object
	T mValue;
	
	
	//Construct a Node containing an object
	public Node(T obj)
	{
		mValue = obj;
	}
	
	//Copy Constructor
	public Node(Node<T> n)
	{
		try
		{
			mValue = (T)n.clone();
			
		}
	}
	
	//Get the node object
	public T getValue()
	{
		return mValue;
	}
	
	//Need to override equal and hash methods
	
}
