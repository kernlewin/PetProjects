package ksk.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * This defines the operations supported on complex numbers.
 * 
 *  The class uses my GeneralNumber class to support operations between any primitive numeric
 *  types, or BigInteger or BigDecimal (or combinations) without using unnecessary storage, losing
 *  accuracy, or requiring casting.
 *  
 * This is (intended to be) an immutable class
 */

//Make the class final; subclasses could make it mutable
public final class Complex {
	//All fields private and final
	private final GeneralNumber mReal;
	private final GeneralNumber mImaginary;

	//Create a complex number.  Note that due to Autoboxing,
	//this constructor can take any primitive numeric type (byte, short, long, int, float or double)
	//as well as BigInteger and BigDecimal objects.
	public Complex(Number real, Number imaginary)
	{
		mReal = new GeneralNumber(real);
		mImaginary = new GeneralNumber(imaginary);
	}

	//Getters:  Note that the GeneralNumber class is immutable; don't have to worry about
	//exposing anything here
	public Number getReal()
	{
		return mReal.getValue();
	}

	public Number getImaginary()
	{
		return mImaginary.getValue();
	}

	//String representation
	//Make sure to handle cases where imaginary part is negative or 1, or where either part is zero.
	public String toString()
	{
		//Character to separate the real and imaginary parts
		char separator;

		//Boolean values indicating whether or not the real and imaginary parts are being shown
		//(i.e. are non-zero)
		boolean realNonZero;
		boolean imaginaryNonZero;
		boolean imaginaryOne;

		//Get a String representation of the imaginary part
		String imaginaryStr = mImaginary.toString().trim();

		//Get a BigDecimal representation of the imaginary part (to avoid overflow)
		BigDecimal imaginaryBD = new BigDecimal( mImaginary.toString());

		//Get a BigDecimal representation of the real part
		BigDecimal realBD = new BigDecimal( mReal.toString());

		//Check if the real part is zero
		if (realBD.compareTo(BigDecimal.ZERO) == 0)
		{
			realNonZero = false;
		}
		else
		{
			realNonZero=true;
		}

		//Check if the imaginary part is zero
		if (imaginaryBD.compareTo(BigDecimal.ZERO) == 0)
		{
			imaginaryNonZero = false;
		}
		else
		{
			imaginaryNonZero = true;
		}

		//Check if the imaginary part is negative
		if (imaginaryBD.compareTo(BigDecimal.ZERO) < 0)
		{
			separator = '-';
			//Remove negative sign
			imaginaryStr = imaginaryStr.substring(imaginaryStr.indexOf('-')+1, imaginaryStr.length());
		}
		else
		{
			separator = '+';
		}

		//Check if the imaginary part is one
		//Note that the sign should be gone
		if (imaginaryBD.compareTo(BigDecimal.ONE) == 0)
		{
			imaginaryOne = true;
		}
		else
		{
			imaginaryOne = false;
		}

		//Okay, ready to build the result String
		String result = "";

		//Show real part
		if (realNonZero)
		{
			result += mReal.toString();
		}

		//Show separator (+/-)
		if ((realNonZero)&&(imaginaryNonZero))
		{
			result += " " + separator + " ";
		}
		
		//Show imaginary part
		if (imaginaryNonZero)
		{
			if (imaginaryOne)
			{
				result += "i";
			}
			else
			{
				result += imaginaryStr + "i";
			}
		}
		
		//If neither part was shown, value is 0
		if ((!realNonZero)&&(!imaginaryNonZero))
		{
			result = "0";
		}
		//Done!
		return result;
	}


	//Operators.  Note that ALL of these create a new Complex number with the resultant value.
	//This class is immutable
	public Complex add(Complex arg)
	{
		Number realResult = null;
		Number imaginaryResult = null;

		switch (mType)
		{
		case INTEGER:
			realResult = new Long( getReal().longValue() + arg.getReal().longValue());
			imaginaryResult = new  Long( getImaginary().longValue() + arg.getImaginary().longValue());
			break;
		case BIGINTEGER:
			BigInteger arg1 = (BigInteger)getReal();
			BigInteger arg2 = (BigInteger)arg.getReal();
			realResult = arg1.add(arg2);

			arg1 = (BigInteger)getImaginary();
			arg2 = (BigInteger)arg.getImaginary();
			imaginaryResult = arg1.add(arg2);
			break;
		case BIGREAL:
			BigDecimal arg3 = (BigDecimal)getReal();
			BigDecimal arg4 = (BigDecimal)arg.getReal();
			realResult = arg3.add(arg4);

			arg3 = (BigDecimal)getImaginary();
			arg4 = (BigDecimal)arg.getImaginary();
			imaginaryResult = arg3.add(arg4);
			break;
		default:  //OpType.REAL
			realResult = new Double( getReal().doubleValue() + arg.getReal().doubleValue());
			imaginaryResult = new  Double( getImaginary().doubleValue() + arg.getImaginary().doubleValue());
			break;			
		}

		return new Complex(realResult, imaginaryResult);
	}

	public Complex subtract(Complex arg)
	{
		Number realResult = null;
		Number imaginaryResult = null;

		switch (mType)
		{
		case INTEGER:
			realResult = new Long( getReal().longValue() - arg.getReal().longValue());
			imaginaryResult = new  Long( getImaginary().longValue() - arg.getImaginary().longValue());
			break;
		case BIGINTEGER:
			BigInteger arg1 = (BigInteger)getReal();
			BigInteger arg2 = (BigInteger)arg.getReal();
			realResult = arg1.subtract(arg2);

			arg1 = (BigInteger)getImaginary();
			arg2 = (BigInteger)arg.getImaginary();
			imaginaryResult = arg1.subtract(arg2);
			break;
		case BIGREAL:
			BigDecimal arg3 = (BigDecimal)getReal();
			BigDecimal arg4 = (BigDecimal)arg.getReal();
			realResult = arg3.subtract(arg4);

			arg3 = (BigDecimal)getImaginary();
			arg4 = (BigDecimal)arg.getImaginary();
			imaginaryResult = arg3.subtract(arg4);
			break;
		default:  //OpType.REAL
			realResult = new Double( getReal().doubleValue() - arg.getReal().doubleValue());
			imaginaryResult = new  Double( getImaginary().doubleValue() - arg.getImaginary().doubleValue());
			break;			
		}

		return new Complex(realResult, imaginaryResult);

	}

	public Complex multiply(Complex arg)
	{
		 //Note:  (a + bi)(c + di) = (ac-bd) + (ad+bc)i
	
		switch (mType)
		{
		case INTEGER:
			break;
		case BIGINTEGER:
			break;
		case BIGREAL:
			break;
		default:  //OpType.REAL
		}

		return null;
	}

	public Complex divide(Complex arg)
	{
		//Using difference of squares to make the denominator real:
		//(a + bi)/(c + di) = [(ac+bd) + (bc-ad)i] / [c^2 + d^2]
		
		switch (mType)
		{
		case INTEGER:
			break;
		case BIGINTEGER:
			break;
		case BIGREAL:
			break;
		default:  //OpType.REAL
		}

		return null;
	}

	public Complex power(double exp)
	{
		switch (mType)
		{
		case INTEGER:
			break;
		case BIGINTEGER:
			break;
		case BIGREAL:
			break;
		default:  //OpType.REAL
		}

		return null;
	}

}
