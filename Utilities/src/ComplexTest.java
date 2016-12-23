import java.math.BigDecimal;
import java.util.Scanner;

import ksk.math.Complex;


public class ComplexTest {

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		while (true)
		{
			
			//Get real and imaginary parts
			System.out.print("Real: ");
			int real = sc.nextInt();

			System.out.print("Imaginary: ");
			int imaginary = sc.nextInt();

			//Create a complex number
			Complex num = new Complex(real, imaginary);

			System.out.println(num.toString());
			
			
		}
	}
}
