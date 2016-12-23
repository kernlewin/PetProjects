package ksk.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/*
 *Honestly; what little advantage there is in trying to minimize the size of the
 *underlying numeric type seems to be grossly outweighed by the added complexity and
 *hackiness of constant typechecking.
 *
 *Okay, take three; given that BigDecimal already supports exact values for mathematical operations of
 *between arguments with any precision with ONE exception, the goal of this class is... to deal with
 *that one exception.  And to make the difference between integers and decimals transparent.
 *
 *So the only real limitation on BigDecimal is that it can't handle exact values for division when
 *the result is an infinitely repeating decimal.
 *
 *So in this class, we will represent all numbers as BigInteger improper fractions, with support for
 *displaying either decimal approximations, improper fractions or mixed numbers.
 *Numbers will automatically be reduced to lowest terms.
 *
 *Rounding will ONLY be performed for the purpose of creating a decimal approximation.
 *
 *This class is IMMUTABLE
 */

public class BigNumber extends Number
{
	//Internally, this class uses a pair of BigInteger values to exactly represent any rational value,
	//Including those that would lead to infinitely repeating decimals
	//Only irrational values (infinite, non-repeating) will have approximate values, and they will
	//still retain all the precision that have when entered.

	//I'll probably add some constants to the class, with >100 digits of precision

	private BigInteger mNumerator;
	private BigInteger mDenominator;

	private boolean mIsInteger;

	//Constructor:  Copy Constructor
	public BigNumber(Number src)
	{
		this(src.toString());
	}

	//Constructor:  From String
	public BigNumber(String src)
	{
		super();

		try
		{
			//Convert the source Number to a BigDecimal
			BigDecimal dec = new BigDecimal(src);

			//The scale of the BigDecimal tells us how many digits are after the decimal place.
			//Therefore we can think of the BigDecimal as the ratio of two integers:
			//Unscaled_value / 10^scale
			mNumerator = dec.unscaledValue();
			mDenominator = BigInteger.TEN.pow(dec.scale());
		}
		catch (NumberFormatException e)
		{
			//Not a valid Numb
			mNumerator = BigInteger.ZERO;
			mDenominator = BigInteger.ONE;
		}
	}

//Constructor:  From Number (including primitives, BigInteger, and BigDecimal)

//Arithmetic operations

//Add

//Subtract

//Multiply

//Divide

//Remainder


//Convert to String (decimal approximation)

//Alternate String representations; Mixed or improper

//Conversion methods

//Reduce:  Reduce this value to lowest terms (called only by the constructor, since
//the class is immutable)
private void reduce()
{
	//Just noticed the BigInteger.gcd() method.  Makes this REALLY simple
	BigInteger commonDivisor = mNumerator.gcd(mDenominator);

	mNumerator = mNumerator.divide(commonDivisor);
	mDenominator = mDenominator.divide(commonDivisor);
}

}




/*********************  OLD VERSION  ***************************/
/*
 * This immutable class encapsulates a number whose underlying class may be any of the Java 
 * numberic primitive types, or BigInteger or BigDecimal.
 * 
 * It allows arithmetic operations (+,-,*,/) on numbers without changing the type of the result.
 * For example, if the underlying type is an Integer, then integer operations will be performed,
 * and the number will overflow like an integer.
 * 
 * Arithmetic operations between GeneralNumbers with different underlying types results in a
 * GeneralNumber whose underlying type is the smallest type that can hold the resulting value
 * without overflow.  The class also guarantees that if the arguments are both integers, then the result
 * will be an integer.  Otherwise the result will be a floating-point type.     
 * Note that the second pathway is used if EITHER argument is floating-point type.
 * 
 * If the type given in creating a GeneralNumber is not one of the supported types, the default is
 * BigDecimal
 * 
 * Although the class provides methods for getting the value and type of the underlying Number
 * object, for most applications, all calculations and comparisons should be done with the methods of
 * this class.  The final result can be output using the toString() method
 */

/*
//This class explicitly supports Byte, Short, Long, Integer, Float, Double, BigInteger, BigDecimal
public class GeneralNumber implements Comparable<GeneralNumber> {
	//Include convenience constants for a couple of common values for use in comparisons and operations
	//Use the smallest possible data type to avoid artificially inflating memory use
	public static final GeneralNumber ZERO = new GeneralNumber((byte)0);
	public static final GeneralNumber ONE = new GeneralNumber((byte)1);
	public static final GeneralNumber TWO = new GeneralNumber((byte)2);
	public static final GeneralNumber TEN = new GeneralNumber((byte)10);
	public static final GeneralNumber NEG_ONE = new GeneralNumber((byte)(-1));


	//Remember if this object represents an integer type
	private final boolean mIntegerType;

	//List, in "size" order of supported types
	private enum Types {BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BIGINTEGER, BIGDECIMAL};

	//The type of this object
	private final Types mType;

	//The value of this object
	private final Number mValue;

	//Constructor:  Check the runtime type of the initializing object
	//And store a copy of its value
	public GeneralNumber(Number src)
	{
		//Check type, going from large to small
		if (src instanceof BigDecimal)
		{
			mIntegerType = false;
			mType = Types.BIGDECIMAL;
			mValue = new BigDecimal(src.toString());
		}
		else if (src instanceof BigInteger)
		{
			mIntegerType = true;
			mType = Types.BIGINTEGER;
			mValue = new BigInteger(src.toString());
		}
		else if (src instanceof Double)
		{
			mIntegerType = false;
			mType = Types.DOUBLE;
			mValue = new Double(src.doubleValue());			
		}
		else if (src instanceof Float)
		{
			mIntegerType = false;
			mType = Types.FLOAT;
			mValue = new Float(src.floatValue());
		}
		else if (src instanceof Long)
		{
			mIntegerType = true;
			mType = Types.LONG;
			mValue = new Long(src.longValue());			
		}
		else if (src instanceof Integer)
		{
			mIntegerType = true;
			mType = Types.INTEGER;
			mValue = new Integer(src.intValue());			
		}
		else if (src instanceof Short)
		{
			mIntegerType = true;
			mType = Types.SHORT;
			mValue = new Short(src.shortValue());			
		}
		else if (src instanceof Byte)
		{
			mIntegerType = true;
			mType = Types.BYTE;
			mValue = new Byte(src.byteValue());			
		}
		else
		{
			//Default to BigDecimal
			mIntegerType = false;
			mType = Types.BIGDECIMAL;
			mValue = new BigDecimal(src.toString());			
		}
	}

	//Getters:  Get the value (as a NUMBER) and type
	public Number getValue()
	{
		//Can't return mValue, or the class becomes mutable
		//(can't change the reference, but can change the number)
		switch (mType)
		{
		case BIGDECIMAL:
			return new BigDecimal(mValue.toString());
		case BIGINTEGER:
			return new BigInteger(mValue.toString());
		case DOUBLE:
			return mValue.doubleValue();  //Return a double and let Java autobox it to Double
		case FLOAT:
			return mValue.floatValue();
		case LONG:
			return mValue.longValue();
		case INTEGER:
			return mValue.intValue();
		case SHORT:
			return mValue.shortValue();
		case BYTE:
			return mValue.byteValue();
		default:  //Shouldn't happen!
			return null;
		}
	}

	public Types getType()
	{
		//Okay to return this (I think) because enum values are implicitly final
		return mType;
	}

	//Check if this number is an integer or not
	public boolean isIntegerType()
	{
		return mIntegerType;
	}

	//Check if this object contains a number of type byte, short, int, etc.
	//False if the underlying value is a BigInteger or BigDecimal
	public boolean isPrimitiveType()
	{
		return !((mType==Types.BIGINTEGER)||(mType==Types.BIGDECIMAL));
	}

	//String representation of this number
	public String toString()
	{
		return mValue.toString();
	}

	//Arithmetic Methods (remember; immutable; all return a new object)
	//All methods return GeneralNumber object of underlying type max(this.mType, arg.mType)
	//with the sole exception that if the "larger" type is BigInteger, and the smaller type is
	//a real-number type, then BigDecimal must be used to avoid loss of information

	//Add
	public GeneralNumber add(GeneralNumber arg)
	{
		//Get the two underlying Number objects
		Number n1 = this.getValue();
		Number n2 = arg.getValue();

		//Determine the return type
		GeneralNumber returnValue = null;
		Types returnType = getPromotedType (this.mType, arg.mType);

		switch (returnType)
		{
		case BYTE:
			returnValue = new GeneralNumber ( n1.byteValue() + n2.byteValue());
			break;
		case SHORT:
			returnValue = new GeneralNumber(n1.shortValue() + n2.shortValue());
			break;
		case INTEGER:
			returnValue = new GeneralNumber(n1.intValue() + n2.intValue());
			break;
		case LONG:
			returnValue = new GeneralNumber (n1.longValue() + n2.longValue());
			break;
		case FLOAT:
			returnValue = new GeneralNumber(n1.floatValue() + n2.floatValue());
			break;
		case DOUBLE:
			returnValue = new GeneralNumber(n1.doubleValue() + n2.doubleValue());
			break;
		case BIGINTEGER:
			BigInteger bi1 = new BigInteger(n1.toString());
			BigInteger bi2 = new BigInteger(n2.toString());
			returnValue = new GeneralNumber(bi1.add(bi2));
			break;
		case BIGDECIMAL:
			BigDecimal bd1 = new BigDecimal(n1.toString());
			BigDecimal bd2 = new BigDecimal(n2.toString());
			returnValue = new GeneralNumber(bd1.add(bd2));
			break;				
		}

		return returnValue;
	}

	//Subtract - Add negative
	public GeneralNumber subtract(GeneralNumber arg)
	{
		//Get the two underlying Number objects
		Number n1 = this.getValue();
		Number n2 = arg.getValue();

		//Determine the return type
		GeneralNumber returnValue = null;
		Types returnType = getPromotedType (this.mType, arg.mType);

		switch (returnType)
		{
		case BYTE:
			returnValue = new GeneralNumber ( n1.byteValue() - n2.byteValue());
			break;
		case SHORT:
			returnValue = new GeneralNumber(n1.shortValue() - n2.shortValue());
			break;
		case INTEGER:
			returnValue = new GeneralNumber(n1.intValue() - n2.intValue());
			break;
		case LONG:
			returnValue = new GeneralNumber (n1.longValue() - n2.longValue());
			break;
		case FLOAT:
			returnValue = new GeneralNumber(n1.floatValue() - n2.floatValue());
			break;
		case DOUBLE:
			returnValue = new GeneralNumber(n1.doubleValue() - n2.doubleValue());
			break;
		case BIGINTEGER:
			BigInteger bi1 = new BigInteger(n1.toString());
			BigInteger bi2 = new BigInteger(n2.toString());
			returnValue = new GeneralNumber(bi1.subtract(bi2));
			break;
		case BIGDECIMAL:
			BigDecimal bd1 = new BigDecimal(n1.toString());
			BigDecimal bd2 = new BigDecimal(n2.toString());
			returnValue = new GeneralNumber(bd1.subtract(bd2));
			break;				
		}

		return returnValue;
	}

	//Multiply
	public GeneralNumber multiply(GeneralNumber arg)
	{
		//Get the two underlying Number objects
		Number n1 = this.getValue();
		Number n2 = arg.getValue();

		//Determine the return type
		GeneralNumber returnValue = null;
		Types returnType = getPromotedType (this.mType, arg.mType);

		switch (returnType)
		{
		case BYTE:
			returnValue = new GeneralNumber ( n1.byteValue() * n2.byteValue());
			break;
		case SHORT:
			returnValue = new GeneralNumber(n1.shortValue() * n2.shortValue());
			break;
		case INTEGER:
			returnValue = new GeneralNumber(n1.intValue() * n2.intValue());
			break;
		case LONG:
			returnValue = new GeneralNumber (n1.longValue() * n2.longValue());
			break;
		case FLOAT:
			returnValue = new GeneralNumber(n1.floatValue() * n2.floatValue());
			break;
		case DOUBLE:
			returnValue = new GeneralNumber(n1.doubleValue() * n2.doubleValue());
			break;
		case BIGINTEGER:
			BigInteger bi1 = new BigInteger(n1.toString());
			BigInteger bi2 = new BigInteger(n2.toString());
			returnValue = new GeneralNumber(bi1.multiply(bi2));
			break;
		case BIGDECIMAL:
			BigDecimal bd1 = new BigDecimal(n1.toString());
			BigDecimal bd2 = new BigDecimal(n2.toString());
			returnValue = new GeneralNumber(bd1.multiply(bd2));
			break;				
		}

		return returnValue;		
	}

	//Divide
	public GeneralNumber divide(GeneralNumber arg)
	{
		//Get the two underlying Number objects
		Number n1 = this.getValue();
		Number n2 = arg.getValue();

		//Determine the return type
		GeneralNumber returnValue = null;
		Types returnType = getPromotedType (this.mType, arg.mType);

		switch (returnType)
		{
		case BYTE:
			returnValue = new GeneralNumber ( n1.byteValue() / n2.byteValue());
			break;
		case SHORT:
			returnValue = new GeneralNumber(n1.shortValue() / n2.shortValue());
			break;
		case INTEGER:
			returnValue = new GeneralNumber(n1.intValue() / n2.intValue());
			break;
		case LONG:
			returnValue = new GeneralNumber (n1.longValue() / n2.longValue());
			break;
		case FLOAT:
			returnValue = new GeneralNumber(n1.floatValue() / n2.floatValue());
			break;
		case DOUBLE:
			returnValue = new GeneralNumber(n1.doubleValue() / n2.doubleValue());
			break;
		case BIGINTEGER:
			BigInteger bi1 = new BigInteger(n1.toString());
			BigInteger bi2 = new BigInteger(n2.toString());
			returnValue = new GeneralNumber(bi1.divide(bi2));
			break;
		case BIGDECIMAL:
			BigDecimal bd1 = new BigDecimal(n1.toString());
			BigDecimal bd2 = new BigDecimal(n2.toString());
			returnValue = new GeneralNumber(bd1.divide(bd2));
			break;				
		}

		//Now find the smallest type that we can use to hold the result
		if (returnValue.isIntegerType())
		{
			if (checkRange(returnValue, Types.BYTE))
			{
				return new GeneralNumber(returnValue.getValue().byteValue());
			}
			else if (checkRange(returnValue, Types.SHORT))
			{
				return new GeneralNumber(returnValue.getValue().shortValue());
			}
			else if (checkRange(returnValue, Types.INTEGER))
			{
				return new GeneralNumber(returnValue.getValue().intValue());
			}
			else if (checkRange(returnValue, Types.LONG))
			{
				return new GeneralNumber(returnValue.getValue().longValue());
			}
			else
			{
				return new GeneralNumber( new BigInteger(returnValue.getValue().toString()));
			}
		}
		else
		{
			//Floating point types
			if (checkRange(returnValue, Types.FLOAT))
			{
				return new GeneralNumber(returnValue.getValue().floatValue());
			}
			else if (checkRange(returnValue, Types.DOUBLE))
			{
				return new GeneralNumber(returnValue.getValue().doubleValue());
			}
			else
			{
				return new GeneralNumber( new BigDecimal(returnValue.getValue().toString()));
			}
		}

		//Shouldn't get here!
	}

	//Power - Note that even for this method we assume that the result will fit in a return type that
	//is the larger of the argument types.  It's up to the caller to deal with the possibility of overflow
	public GeneralNumber pow(GeneralNumber arg)
	{
		//Get the two underlying Number objects
		Number n1 = this.getValue();
		Number n2 = arg.getValue();

		//Determine the return type
		GeneralNumber returnValue = null;
		Types returnType = getPromotedType (this.mType, arg.mType);

		switch (returnType)
		{
		case BYTE:
			returnValue = new GeneralNumber ( (byte)Math.pow(n1.byteValue(), n2.byteValue()) );
			break;
		case SHORT:
			returnValue = new GeneralNumber(n1.shortValue() + n2.shortValue());
			break;
		case INTEGER:
			returnValue = new GeneralNumber(n1.intValue() + n2.intValue());
			break;
		case LONG:
			returnValue = new GeneralNumber (n1.longValue() + n2.longValue());
			break;
		case FLOAT:
			returnValue = new GeneralNumber(n1.floatValue() + n2.floatValue());
			break;
		case DOUBLE:
			returnValue = new GeneralNumber(n1.doubleValue() + n2.doubleValue());
			break;
		case BIGINTEGER:
			BigInteger bi1 = new BigInteger(n1.toString());
			BigInteger bi2 = new BigInteger(n2.toString());
			returnValue = new GeneralNumber(bi1.add(bi2));
			break;
		case BIGDECIMAL:
			BigDecimal bd1 = new BigDecimal(n1.toString());
			BigDecimal bd2 = new BigDecimal(n2.toString());
			returnValue = new GeneralNumber(bd1.add(bd2));
			break;				
		}

		return returnValue;
	}

	//Mini helper-methods

	//Given the types of two arguments, figure out what type we should use to
	//Do calculations.  We will always use a "larger" type than the two arguments in case of
	//overflow
	private Types getPromotedType(Types t1, Types t2)
	{
		//Get the "larger" enum
		Types result = (t1.compareTo(t2)<0)?t2:t1;

		//In general, all of the floating-point types are "larger" than all of the integer types
		//The only exception is BigInteger.  If BigInteger is mixed with float or double,
		//we need to return a BigDecimal
		boolean floatingPoint = ((t1==Types.FLOAT)||(t1==Types.DOUBLE)||(t2==Types.FLOAT)||(t2==Types.DOUBLE));

		if ((result == Types.BIGINTEGER)&&(floatingPoint))
		{
			result = Types.BIGDECIMAL;
		}

		return result;
	}

	//Check whether or not the selected value fits into the MIN/MAX range of a
	//specific type; used in conjunction with getPromotedType().  After a calculation,
	//see if we can "demote" the promoted type back down to the original types of the
	//arguments.
	private boolean checkRange(GeneralNumber value, Types type)
	{

		//"type" represents the Type we want to fit the value into

		//First of all, if the type is a "big" type, everything fits
		if ((type==Types.BIGINTEGER)||(type==Types.BIGDECIMAL))
		{
			return true;
		}

		//For everything else, do a range check using BigDecimal
		BigDecimal check = new BigDecimal(value.getValue().toString());

		//Get the min/max range for "type"
		double min, max;

		switch(type)
		{
		case BYTE:
			min = Byte.MIN_VALUE;
			max = Byte.MAX_VALUE;
			break;
		case SHORT:
			min = Short.MIN_VALUE;
			max = Short.MAX_VALUE;
			break;
		case INTEGER:
			min = Integer.MIN_VALUE;
			max = Integer.MAX_VALUE;
			break;
		case LONG:
			min = Long.MIN_VALUE;
			max = Long.MAX_VALUE;
			break;
		case FLOAT:
			min = -Float.MAX_VALUE;
			max = Float.MAX_VALUE;
			break;
		case DOUBLE:
			min = -Double.MAX_VALUE;
			max = Double.MAX_VALUE;
			break;
		default:
			return false;
		}

		//Compare the value to the bounds
		if ((check.compareTo(new BigDecimal(min)) >= 0)&&(check.compareTo(new BigDecimal(max)) <= 0))
		{
			return true;
		}

		//Out of bounds
		return false;
	}

	//Comparable interface
	public int compareTo (GeneralNumber obj)
	{
		if ( (this.isPrimitiveType())||(obj.isPrimitiveType()) )
		{
			//For primitive types, it's (probably) easier to calculate the difference, as doubles, and then
			//return the sign of the difference
			double n1 = this.getValue().doubleValue();
			double n2 = this.getValue().doubleValue();
			return (int)Math.signum(n1-n2);
		}
		else
		{
			//If either object is of a "Big" type, then just use the BigDecimal compareTo method
			BigDecimal bd1 = new BigDecimal(this.getValue().toString());
			BigDecimal bd2 = new BigDecimal(obj.getValue().toString());

			return bd1.compareTo(bd2);
		}
	}
}
 */