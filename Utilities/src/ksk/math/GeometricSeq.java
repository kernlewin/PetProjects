package ksk.math;

public class GeometricSeq extends Sequence{

	long mFirstTerm;
	long mRatio;
	
	public GeometricSeq(long a, long r)
	{
		mFirstTerm = a;
		mRatio = r;
	}
	
	public long getRatio()
	{
		return mRatio;
	}
	
	@Override
	public long getTerm(long n) {
		
		long a = getTerm(1);
		long r = getTerm(2) / a;
		
		return a*(long)Math.pow(r, n-1);
	}

}
