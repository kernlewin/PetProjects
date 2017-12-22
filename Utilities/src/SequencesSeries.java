import ksk.math.FibonacciSeq;
import ksk.math.Sequence;


public class SequencesSeries {

	public static void main(String[] args)
	{
		Sequence seq = new FibonacciSeq();

		for (int i=1; i<=1000; i++)
		{
			System.out.print(" " + seq.getTerm(i));

			if (i%10==0)
			{
				System.out.print("\n");
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}
}
