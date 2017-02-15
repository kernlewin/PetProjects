package ksk.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

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

public class BigNumber extends Number implements Comparable<BigNumber>
{
	//Internally, this class uses a pair of BigInteger values to exactly represent any rational value,
	//Including those that would lead to infinitely repeating decimals
	//Only irrational values (infinite, non-repeating) will have approximate values, and they will
	//still retain all the precision that have when entered.

	//I'll probably add some constants to the class, with >100 digits of precision

	//Choose how many decimal places to show when the true answer is repeating infinitely
	public static final int DIGITS_TO_SHOW_WHEN_REPEATING = 10;
	
	//Some common values
	public static final BigNumber ZERO = new BigNumber(0);
	public static final BigNumber ONE = new BigNumber(1);
	public static final BigNumber TEN = new BigNumber(10);

	private BigInteger mNumerator;
	private BigInteger mDenominator;

	//Constructor:  Copy Constructor
	public BigNumber(BigNumber src)
	{
		mNumerator = new BigInteger(src.mNumerator.toString());
		mDenominator = new BigInteger(src.mDenominator.toString());

		reduce();
	}

	//Constructor:  From String	
	public BigNumber(String src)
	{
		super();

		//Convert the source Number to a BigDecimal
		//Note that this could throw a NumberFormatException, but there's nothing we can do about it
		//anyway, so we don't catch it here
		BigDecimal dec = new BigDecimal(src);

		//The scale of the BigDecimal tells us how many digits are after the decimal place.
		//Therefore we can think of the BigDecimal as the ratio of two integers:
		//Unscaled_value / 10^scale
		mNumerator = dec.unscaledValue();
		mDenominator = BigInteger.TEN.pow(dec.scale());

		reduce();
	}

	//Constructor:  From Number (including primitives, BigInteger, and BigDecimal)
	public BigNumber (Number src)
	{
		this(src.toString());
	}

	//Constructor from numerator and denominator
	public BigNumber(Number num, Number denom)
	{
		mNumerator = new BigInteger(num.toString());
		mDenominator = new BigInteger(denom.toString());
		
		reduce();
	}

	//Arithmetic operations

	//Add
	public BigNumber add(BigNumber arg)
	{
		//We're going to reduce this anyway, so just use the product of the denominators
		//as the common denominator


		BigInteger denominator = this.mDenominator.multiply(arg.mDenominator);

		//Calculate the two numerators
		BigInteger num1 = this.mNumerator.multiply(arg.mDenominator);
		BigInteger num2 = this.mDenominator.multiply(arg.mNumerator);

		//Add the numerators
		BigInteger numerator = num1.add(num2);

		return new BigNumber(numerator, denominator);
	}

	//Subtract
	public BigNumber subtract(BigNumber arg)
	{
		//Add the negation
		return this.add(arg.negate());
	}

	//Multiply
	public BigNumber multiply(BigNumber arg)
	{
		//Multiply numerators
		BigInteger numerator = this.mNumerator.multiply(arg.mNumerator);

		//Multiply denominators
		BigInteger denominator = this.mDenominator.multiply(arg.mDenominator);

		return new BigNumber(numerator, denominator);
	}

	//Divide
	public BigNumber divide(BigNumber arg)
	{
		//Check for division by zero
		if (arg.compareTo(BigNumber.ZERO)==0)
		{
			throw new ArithmeticException("Division by zero error in BigNumber class");
		}

		//Multiply by reciprocal
		return this.multiply(arg.reciprocal());
	}

	//Remainder

	//Absolute Value
	public BigNumber abs()
	{
		if (signum()<0)
		{
			return negate();
		}
		else
		{
			return new BigNumber(this);
		}
	}

	//Check the sign of the number; -1, 0 or 1
	public int signum()
	{
		//Check sign of numerator and denominator
		int numerator_signum = mNumerator.signum();
		int denominator_signum = mDenominator.signum();

		//Numerator is zero
		if (numerator_signum==0)
		{
			return 0;
		}
		//Signs are different
		else if ((numerator_signum != denominator_signum))
		{
			return -1;
		}
		else 
			//Signs are the same
		{
			return 1;
		}

	}

	//Power
	/*
	public BigNumber power(BigNumber arg)
	{
		//BigInteger only supports power operations with int exponents, so a little trickery is necessary

		//First, create a BigNumber that contains the value of the largest possible integer
		BigNumber intMax = BigNumber.valueOf(Integer.MAX_VALUE);

		//Next, to avoid complications, make the power positive
		BigNumber base = this;
		if (arg.compareTo(BigNumber.ZERO)<0)
		{
			base = reciprocal();
		}
		arg = arg.abs();

		//Okay, now to begin.
		//As long as the exponent is greater than intMax, keep calculating that power and
		//multiplying it into the result
		BigNumber result = BigNumber.ONE;
		BigNumber maxPower = new BigNumber (base.mNumerator.pow(Integer.MAX_VALUE), 
				base.mDenominator.pow(Integer.MAX_VALUE));
		while (arg.compareTo(intMax) >= 0)
		{
			//Multiply by base^intMax
			result = result.multiply(maxPower);

			//Reduce the remaining exponent
			arg = arg.subtract(intMax);
		}
	}
	 */

	//Caculate the negative of this number
	public BigNumber negate()
	{
		return new BigNumber(mNumerator.negate(), mDenominator);
	}

	//Reciprocal
	public BigNumber reciprocal()
	{
		return new BigNumber(mDenominator, mNumerator);
	}

	//Convert to BigDecimal (USUALLY exact, but not for infinitely repeating decimals)
	public BigDecimal getValue()
	{
		//Create decimal versions of numerator and denominator
		BigDecimal numerator = new BigDecimal(mNumerator);
		BigDecimal denominator = new BigDecimal(mDenominator);

		//Try to divide exactly
		try
		{
			return numerator.divide(denominator);
		}
		catch (ArithmeticException e)
		{
			//Couldn't get an exact result; round
			return numerator.divide(denominator, new MathContext(DIGITS_TO_SHOW_WHEN_REPEATING,RoundingMode.HALF_EVEN));
		}
	}

	//Get improper fraction
	//Returns an array containing numerator, denominator
	public BigInteger[] getFraction()
	{
		return new BigInteger[]{mNumerator, mDenominator};
	}

	//Get mixed number
	//Returns an array containing whole part, numerator denominator
	public BigInteger[] getMixedNumber()
	{
		BigInteger[] result = new BigInteger[3];

		//The integer part of the mixed number is just the integer value of
		//numerator/denominator
		//New numerator is the remainder
		BigInteger[] divided = mNumerator.divideAndRemainder(mDenominator);

		//Make the "remainder" positive
		divided[1] = divided[1].abs();

		//Done!
		result[0] = divided[0];  //Integer
		result[1] = divided[1];  //Numerator
		result[2] = mDenominator; //Denominator

		return result;
	}
	//Convert to String (exact if possible)
	public String toString()
	{
		return getValue().toString();
	}

	//Get a String with decimal places specified
	public String toRoundedString(int n)
	{
		return getValue().round(new MathContext(n, RoundingMode.HALF_EVEN)).toString();
	}

	//Get a possibly improper fraction String
	public String toFractionString()
	{
		String result = mNumerator.toString();

		//If the numerator is zero or the denominator is one, we're done
		if ((mNumerator.compareTo(BigInteger.ZERO) != 0)&&(mDenominator.compareTo(BigInteger.ONE) != 0))
		{
			result += "/" + mDenominator.toString();
		}
		return result;
	}

	//Get a mixed number String
	public String toMixedString()
	{
		String result = "";

		BigInteger[] mixed = getMixedNumber();

		////Include integer part only if it's non-zero
		if (mixed[0].compareTo(BigInteger.ZERO) != 0)
		{
			result += mixed[0].toString();
		}

		//Include the fraction if the numerator is non-zero
		if (mixed[1].compareTo(BigInteger.ZERO) != 0)
		{
			result += new BigNumber(mixed[1], mixed[2]).toFractionString();
		}

		//Done!
		return result;

	}

	//Reduce:  Reduce this value to lowest terms (called only by the constructor, since
	//the class is immutable)
	//NOTE:  Do not call ANY BigNumber class methods here!
	//Most of them require construction of additional objects, which can lead to infinite loops
	private void reduce()
	{	

		//SPECIAL CASE:
		//If the numerator is zero, then just change the denominator to 1;
		//Otherwise we will end up checking the sign of the numerator and denominator, which
		//requires comparison to zero, which makes it impossible for BigNumber.ZERO to construct
		//itself!
		if (mNumerator.compareTo(BigInteger.ZERO)==0)
		{
			mDenominator = BigInteger.ONE;
			return;
		}

		//Just noticed the BigInteger.gcd() method.  Makes this REALLY simple
		BigInteger commonDivisor = mNumerator.gcd(mDenominator);

		mNumerator = mNumerator.divide(commonDivisor);
		mDenominator = mDenominator.divide(commonDivisor);

		//If the value is negative, make the numerator negative
		//To avoid using methods like BigNumber.compareTo, I will instead see if the product of the
		//numerator and denominator is positive or negative, using BigInteger.compareTo
		if (mNumerator.multiply(mDenominator).compareTo(BigInteger.ZERO) < 0)
		{
			mNumerator = mNumerator.abs().negate();
			mDenominator = mDenominator.abs();
		}
		else  //Otherwise, make sure both are positive (avoid double-negative fractions!)
		{
			mNumerator = mNumerator.abs();
			mDenominator = mDenominator.abs();			
		}
	}

	//Override method inherited from Comparable
	@Override
	public int compareTo(BigNumber arg) {
		//Subtract and return the sign
		return this.subtract(arg).signum();
	}


	//Override methods inherited from Number	
	@Override
	public double doubleValue() {
		return Double.valueOf(toString());
	}

	@Override
	public float floatValue() {
		return Float.valueOf(toString());
	}

	@Override
	public int intValue() {
		return Integer.valueOf(toString());
	}

	@Override
	public long longValue() {
		return Long.valueOf(toString());
	}
}
