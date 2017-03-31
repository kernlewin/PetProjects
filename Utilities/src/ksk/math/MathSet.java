package ksk.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * Represents the concept of a mathematical set
 * 
 * This base class supports both finite discrete and finite or infinite continuous sets.  The package defines subclasses
 * that implement complements, unions and intersections.
 * 
 * Child classes can potentially support more complex sets, such as infinite but discrete sets
 * (e.g. the set of all even numbers, or the set of all prime numbers)
 * 
 * To permit this, this class will NOT implement any method for listing the members of a set,
 * only for checking membership.
 * 
 * Note that this class is IMMUTABLE
 */

public class MathSet<N extends Number> {	
	
	//Is this set continuous or discrete
	protected boolean mIsContinuous;
	
	//Fields for a continuous set
	N mMin;
	N mMax;
	
	//Field for a discrete set
	List<N> mValues;
	
	//Constructor for a continuous set
	public MathSet(N min, N max)
	{
		mMin = min;
		mMax = max;
		mValues = null;
		mIsContinuous = true;
	}
	
	//Constructor for a discrete set
	public MathSet(List<N> values)
	{
		mMin = null;
		mMax = null;
		mValues = new ArrayList<N>(values); //Copy the list
		mIsContinuous = false;
	}
	
	//Check if the set is continuous
	public final boolean isContinuous()
	{
		return mIsContinuous;
	}
	
	public final boolean isDiscrete()
	{
		return !isContinuous();
	}
	
	//Check min and max of set
	public final N getMinValue()
	{
		return mMin;
	}
	
	public final N getMaxValue()
	{
		return mMax;
	}
	
	//Check whether a given number is a member of the set
	//This is the one method that can be overridden
	public boolean isMember(N value)
	{
		if (mIsContinuous)
		{
			//Check if the number is in range
			//Note that Number does not implement Comparable<> so we will compare by first
			//converting values to BigDecimals
			BigDecimal min = new BigDecimal(mMin.toString());
			BigDecimal max = new BigDecimal(mMax.toString());
			BigDecimal val = new BigDecimal(value.toString());
			
			if ((min.compareTo(val) <= 0 )&&(max.compareTo(val) >= 0))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			//Discrete
			return mValues.contains(value);
		}
	}
	
}
