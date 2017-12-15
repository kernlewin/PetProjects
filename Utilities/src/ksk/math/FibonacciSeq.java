package ksk.math;

import java.util.ArrayList;
import java.util.List;

public class FibonacciSeq extends Sequence	{

	//Cache; computing Fibonacci without one is a fool's game because you end up recomputing the same values over and over
	//again
	List<Long> mCache;

	public FibonacciSeq()
	{
		mCache = new ArrayList<Long>();
		mCache.add(0L);  //Placeholder
		mCache.add(1L);  //Fib(1) = 1
		mCache.add(1L);  //Fib(2) = 1
	}

	@Override
	public long getTerm(long n) {		

		//Update the cache to the present value, if required
		for (int i=mCache.size(); i<=n; i++)
		{
				//Fib(N) = Fib(N-1) + Fib(N-2)
				mCache.add(mCache.get(i-1) + mCache.get(i-2));
		}

		//Value is now guaranteed to be in the cache
		//Technically, this is not a safe cast, but the Fibonacci sequence will exceed the capacity of a "long", well before
		//the index exceeds the capacity of an "int"
		return mCache.get((int)n);
	}


}
