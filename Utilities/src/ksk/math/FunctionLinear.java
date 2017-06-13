package ksk.math;

public class FunctionLinear<N extends Number> extends Function<N> {

	N mSlope;
	BigNumber mIntercept;  //Note that for vertical lines this is the x-intercept.  In all other cases it is the y-intercept
	
	//Flag vertical lines
	boolean mIsVertical;
	
	//Constructor:  Two points
	public FunctionLinear()
	{
		
	}
	
	public N getValue(N x)
	{
		
	}
}
