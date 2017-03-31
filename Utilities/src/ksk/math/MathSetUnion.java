package ksk.math;

//This MathSet represents a union of two sets.
//A number is a member of this set if it is a member of either of the other two sets

public class MathSetUnion<N extends Number> extends MathSet<N> {

	public MathSetUnion (MathSet<N> set1, MathSet<N> set2)
	{
		//Create an empty set
		super(null);
		
		//Set the min and max (min and max of all four bounds)
		//To avoid loss of accuracy/generality, and given that Number is not Comparable,
		//We convert to BigDecimals
		
		
		//Check continuity; union is continuous iff both sets are continuous
		
	}
}
