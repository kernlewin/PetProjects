package ksk.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * This defines the operations supported on complex numbers.
 * 
 *  The class supports any numeric type.  Internally, the class uses
 *  Long, Double, BigInteger or BigDecimal to represent values.
 *  
 *  This makes the class complex, and necessitates run-time type-checking, but allows
 *  integer operations for integers, real operations for reals and "Big" operations for "Big"s
 *  
 * This is (intended to be) an immutable class
 */

//Make the class final; subclasses could make it mutable
public final class Complex {
	//All fields private and final
	private final Number mReal;
	private final Number mImaginary;

	private enum OpType {INTEGER, REAL, BIGINTEGER, BIGREAL};

	private final OpType mType;

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
			mType = OpType.INTEGER;
		}
		else if ((real instanceof BigInteger)&&(imaginary instanceof BigInteger))
		{
			mReal = new BigInteger(((BigInteger)real).toByteArray());
			mImaginary = new BigInteger (((BigInteger)imaginary).toByteArray());
			mType = OpType.BIGINTEGER;
		}
		else if ((real instanceof BigDecimal)&&(imaginary instanceof BigDecimal))
		{
			mReal = new BigDecimal(real.toString());
			mImaginary = new BigDecimal (imaginary.toString());
			mType = OpType.BIGREAL;
		}
		else //For any other type, use Double
		{
			mReal = new Double(real.doubleValue());
			mImaginary = new Double(imaginary.doubleValue());
			mType = OpType.REAL;
		}
	}

	//Getters.  Note that we make copies of the real and imaginary parts to avoid creating
	//external references
	public Number getReal()
	{
		switch (mType)
		{
		case INTEGER:
			return new Long(mReal.longValue());
		case BIGINTEGER:
			return new BigInteger(((BigInteger)mReal).toByteArray());
		case BIGREAL:
			return new BigDecimal(mReal.toString());
		default:  //OpType.REAL
			return new Double(mReal.doubleValue());
		}
	}
	
	public Number getImaginary()
	{
		switch (mType)
		{
		case INTEGER:
			return new Long(mImaginary.longValue());
		case BIGINTEGER:
			return new BigInteger(((BigInteger)mImaginary).toByteArray());
		case BIGREAL:
			return new BigDecimal(mImaginary.toString());
		default:  //OpType.REAL
			return new Double(mImaginary.doubleValue());
		}
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
	
	public Complex multiply(Complex arg)
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

	public Complex divide(Complex arg)
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
