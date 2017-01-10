import java.util.Scanner;

import ksk.math.BigNumber;

/*
 * Test doing basic arithmetic with "BigNumber" objects
 */


public class BigNumberTest {

	public static void main(String[] args)
	{
		//Create a scanner
		Scanner sc = new Scanner(System.in);

		//Variable for the user's input
		String input;

		//Integer variable for menu selections
		int selection = 0;

		//Working value
		BigNumber current = BigNumber.ZERO;

		//Main loop
		do
		{
			System.out.println("Working Value: " + current + "\t" +
					current.toFractionString() + "\t" +
					current.toMixedString());

			System.out.println("\n1) Add\n2) Subtract\n3) Multiply\n4) Divide\n5) ???\n6) Reciprocal\n7) Negate\n\n0) Quit");

			//Get option
			System.out.print("Selection: ");
			input = sc.nextLine();

			//Convert to number
			try
			{
				selection = Integer.valueOf(input);

				if ((selection>=1)&&(selection<=5))
				{
					//These are binary operations; need to get an argument
					System.out.print("Please enter a number: ");
					input = sc.nextLine();

					//Convert to BigNumber; possible exception
					BigNumber argument = new BigNumber(input);

					switch (selection)
					{
					case 1: //Add
						current = current.add(argument);break;
					case 2: //Subtract
						current = current.subtract(argument);break;
					case 3: //Multiply
						current = current.multiply(argument);break;
					case 4: //Divide
						current = current.divide(argument);break;
					case 5: //Power
						//current = current.power(argument);break;
					}
				}
				else if (selection==6)  //Reciprocal
				{
					current = current.reciprocal();
				}
				else if (selection == 7)  //Negate
				{
					current = current.negate();
				}
			}
			catch (NumberFormatException e)
			{
				System.out.println(input + "is not a valid number");
			}

		} while (selection != 0);

		System.out.println("Goodbye!");
		sc.close();
	}
}