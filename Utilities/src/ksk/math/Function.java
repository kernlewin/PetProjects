package ksk.math;

/*
 * This class represents a generic mathematical function that maps values of some
 * independent variable to other values in the same number-space
 * 
 * It can be used with any "Number" type (int, double, BigNumber, etc., including custom classes
 * like my complex number class)
 * 
 * As this class represents a true mathematical function, each independent value maps to a single
 * dependent value, though these can be repeated.
 */

public abstract class Function<N extends Number> {

	//Get the value of the function
	public abstract N getValue(N x);
	
	//Get the domain and range; we are  		
}
