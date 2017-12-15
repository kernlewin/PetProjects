package ksk.math;

public class ArithmeticSeq extends Sequence {

	//An arithmetic sequence is defined by the first term and a common difference
	long mFirstTerm;
	long mDifference;
	
	public ArithmeticSeq(long a, long d)
	{
		mFirstTerm = a;
		mDifference = d;
	}
		
	public long getDifference()
	{
		return mDifference;
	}
	
	@Override
	public long getTerm(long n) {
		
		return mFirstTerm + mDifference*(n-1);
	}

	
	
}
