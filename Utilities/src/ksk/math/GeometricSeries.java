package ksk.math;

public class GeometricSeries extends Series {

	public GeometricSeries(Sequence s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public long Sum(long n)
	{
		long a = getTerm(1);
		long r = getTerm(2)/a;
		
		//Sum = a*(r^n - 1)/(r-1)
		return a*(long)(Math.pow(r,n)-1)/(r-1);
	}
}
