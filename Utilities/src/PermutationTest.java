import java.util.ArrayList;
import java.util.List;

import ksk.util.Permutation;


public class PermutationTest {
	public static void main(String[] args)
	{
		Integer[] numbers = new Integer[10];

		for (int i=0; i<10; i++)
		{
			numbers[i] = new Integer(i+1);
		}

		Permutation<Integer> perms = new Permutation<Integer>(numbers);
		
		while (perms.hasNext())
		{
			List<Integer> order = perms.getOrder();
			
			for (Integer num: order)
			{
				System.out.print(num + " ");
			}
			System.out.println("");
		}
	}
}
