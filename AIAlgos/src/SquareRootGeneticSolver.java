import java.util.Scanner;

import ksk.AI.geneticSolutions.SqrtDNA;


public class SquareRootGeneticSolver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);

		while(true)
		{
			System.out.print("\nPlease enter a number: ");
			double num = sc.nextDouble();

			//Calculate the square root genetically
			double root = SqrtDNA.sqrt(num);
			
			//Output the "solution"
			System.out.println("The square root of " + num + " is about " + root);
		}
	}

}
