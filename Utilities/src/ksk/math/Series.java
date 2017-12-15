package ksk.math;

public class Series {

	Sequence mSequence;
	
	/**
	 * Return the sum of the first n terms of the corresponding sequence
	 * 
	 * @param n  Number of terms to sum
	 * @return  The sum of the first n terms of the corresponding sequence
	 */
	
	public Series (Sequence s)
	{
		mSequence = s;
	}
	
	public long Sum(long n)
	{
		long sum = 0;
		for (long i=1; i<=n; i++)
		{
			sum += getTerm(n);
		}
		
		return sum;
	}
	
	protected long getTerm(long n)
	{
		return mSequence.getTerm(n);
	}
	
}
