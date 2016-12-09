package ksk.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * This defines the operations supported on complex numbers.
 * 
 *  The class uses generics to support any numeric type.  Internally, the class uses
 *  Long, Double, BigInteger or BigDecimal to represent values.
 *  
 *  All operations between long-instantiated complex numbers will be implemented using
 *  integer operations (exact).
 * 
 * This is (intended to be) an immutable class
 */

//Make the class final; subclasses could make it mutable
public final class Complex {
	//All fields private and final

	private final Number mReal;
	private final Number mImaginary;

	//Create a complex number of the specified type
	public Complex(Number real, Number imaginary)
	{
		//Need to create copies of these parameters to avoid this object being modified by
		//external references.

		//Check if the parameters are any of the primitive integer types
		//or BigInteger, or BigDecimal
		if (
				((real instanceof Long)&&(imaginary instanceof Long))||
				((real instanceof Integer)&&(imaginary instanceof Integer))||
				((real instanceof Short)&&(imaginary instanceof Short))||
				((real instanceof Byte)&&(imaginary instanceof Byte))
				)
		{
			//Use Long for ALL integer types
			mReal = new Long(real.longValue());
			mImaginary = new Long (imaginary.longValue());
		}
		else if ((real instanceof BigInteger)&&(imaginary instanceof BigInteger))
		{
			mReal = new BigInteger(((BigInteger)real).toByteArray());
			mImaginary = new BigInteger (((BigInteger)imaginary).toByteArray());			
		}
		else if ((real instanceof BigDecimal)&&(imaginary instanceof BigDecimal))
		{
			mReal = new BigDecimal(((BigDecimal)real).toString());
			mImaginary = new BigDecimal (((BigDecimal)imaginary).toString());			
		}
		else //For any other type, use Double
		{
			mReal = new Double(real.doubleValue());
			mImaginary = new Double(imaginary.doubleValue());
		}
	}

	//Operators.  Note that ALL of these create a new Complex number with the resultant value.
	//This class is immutable
	public Complex<Number> add()
	{
		return null;
	}
}
