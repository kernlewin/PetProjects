package ksk.math;

public class ArithmeticSeries extends Series {

	public ArithmeticSeries(ArithmeticSeq s)
	{
		super(s);
	}
	
	@Override
	public long Sum(long n) {
		
		long a = getTerm(1);
		long d = getTerm(2) - a;
		
		return n*(2*a + (n-1)*d)/2;
	}

}
