package ksk.math;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertEquals;

/*
 * Okay planning a redesign of my bithandling class, levearaging the power of BitSet and
 * BigInteger; between them, they do pretty much everything.
 * 
 * Internally, values will be stored in a boolean array; not space-efficient, but makes bitwise
 * operations really fast.
 * 
 * This class will support:
 * 
 * - Copy Constructor
 * - Construction of a subrange of a BitBlock
 * - Boolean operations (AND, OR, NOT, NAND, NOR, XOR, XNOR)
 * 
 * From BitSet:
 * - Cardinality check (1's count)
 * - Scanning for 1s
 * - Scanning for 0s
 * - Flipping, setting clearing individual bits or ranges
 * 
 * From BigInteger:
 * - Construction from numbers
 * - Construction from strings (any radix)
 * - Conversion to primitive number types
 * - Conversion to BigInteger
 * - Addition, Subtraction, Multiplication, Division, Integer Powers * 
 */

public class BitString {
	private int mLength;
	private BitSet mBits;

	//Constructor:  BitBLock.  Length is in bits
	public BitString(int length)
	{
		mBits = new BitSet();
		mLength = length;
	}

	//Constructor:  BitBlock containing a long value
	public BitString(long val, int length)
	{
		this(new long[]{val}, length);
	}

	//Constructor:  BitBlock containing all of the bits from a long array (overflow bits are ignored)
	//Note that bits are added to the stream in the order that they appear in the array
	public BitString(long[] values, int bitsPerValue)
	{
		if (bitsPerValue<0)
		{
			throw new NegativeArraySizeException();
		}

		//Create the BitSet
		mBits = new BitSet(mLength);

		if (values==null)
		{
			mLength = 0;
		}
		else
		{
			mLength = values.length*bitsPerValue;

			//Wanted to use the BitSet.valueOf(long[]) method, but to allow different numbers of
			//bits per character, we need to do this manually
			int mBitIndex = 0;
			for (int arrayIndex = 0; arrayIndex<values.length; arrayIndex++)
			{
				for (int bit = 0; bit<bitsPerValue; bit++)
				{
					//Get the value of the current bit
					boolean bitValue = (values[arrayIndex] >> (bitsPerValue - bit - 1))%2 == 1;

					//Set the current bit
					mBits.set(mBitIndex++, bitValue);
				}
			}
		}
	}

	public BitString(long[] values)
	{
		this(values, Long.SIZE);
	}

	//Constructor:  Copy
	public BitString(BitString src)
	{
		mLength = src.mLength;
		mBits = new BitSet(mLength);
		mBits.or(src.mBits);
	}

	//Getters/Setters
	public int getLength()
	{
		return mLength;
	}

	//Change the length of this block.  If the new length is shorter, then the
	//most significant bits are lost.  If longer, then false bits are added at the big end
	public void setLength(int length)
	{
		if (length < 0)
		{
			throw new NegativeArraySizeException();
		}

		mLength = length;
		mBits = mBits.get(0, mLength);
	}

	public long[] bits()
	{
		return mBits.toLongArray();
	}

	//Bit-wise methods
	public int cardinality()
	{
		return mBits.cardinality();
	}

	public void clear()
	{
		mBits.clear();
	}

	public void clear(int index)
	{	
		validateIndex(index);
		mBits.clear(index);
	}
	public void clear(int from, int to)
	{
		validateIndex(from);
		validateIndex(to-1);
		mBits.clear(from, to);
	}

	public void flip(int index)
	{
		validateIndex(index);
		mBits.flip(index);
	}

	public void flip(int from, int to)
	{
		validateIndex(from);
		validateIndex(to-1);
		mBits.flip(from, to);
	}
	public boolean get(int index)
	{
		validateIndex(index);

		return mBits.get(index);
	}

	public BitString get(int from,int to)
	{
		validateIndex(from);
		validateIndex(to-1);
		BitSet bitSetRange = mBits.get(from, to);
		BitString bitBlockRange = new BitString(to-from+1);
		bitBlockRange.mBits = bitSetRange;

		return bitBlockRange;

	}

	public int nextSetBit(int index)
	{
		validateIndex(index);
		int result = mBits.nextSetBit(index);

		return checkIndex(result)?result:-1;
	}

	public int nextClearBit(int index)
	{
		validateIndex(index);
		int result = mBits.nextClearBit(index);

		return checkIndex(result)?result:-1;
	}

	public int previousSetBit(int index)
	{
		validateIndex(index);
		int result = mBits.previousSetBit(index);

		return checkIndex(result)?result:-1;
	}

	public int previousClearBit(int index)
	{
		validateIndex(index);
		int result = mBits.previousClearBit(index);

		return checkIndex(result)?result:-1;
	}

	public void set(int index)
	{
		validateIndex(index);
		mBits.set(index);
	}

	public void set(int index, boolean value)
	{
		validateIndex(index);
		mBits.set(index, value);
	}

	public void set(int from, int to)
	{
		validateIndex(from);
		validateIndex(to);
		mBits.set(from, to);
	}

	public void set(int from, int to, boolean value)
	{
		validateIndex(from);
		validateIndex(to);
		mBits.set(from, to, value);
	}



	//Logical operation methods
	public boolean isEmpty()
	{
		return mBits.isEmpty();
	}

	public boolean intersects(BitString block)
	{
		return mBits.intersects(block.mBits);
	}

	public void xor(BitString block)
	{
		mBits.xor(block.mBits);
	}

	public void and(BitString block)
	{
		mBits.andNot(block.mBits);
	}

	public void or(BitString block)
	{
		mBits.or(block.mBits);
	}

	public void not()
	{
		mBits.flip(0, mLength);
	}

	//Shift methods
	public void shiftRight()
	{
		shiftRight(1);
	}

	public void shiftRight(int bits)
	{
		//Create a new BitSet
		BitSet newBits = new BitSet(mLength);

		//Check to see if it's faster to copy over the set or cleared bits
		if (mBits.cardinality() < mLength/2) //Less than half of the bits are set
		{
			//Search for set bits
			int index = mBits.nextSetBit(0);
			while ( index >=0 )
			{
				if (checkIndex(index - bits))
				{
					newBits.set(index - bits);
				}

				index = mBits.nextSetBit(index + 1);
			}
		}
		else
		{
			//Set all of the bits
			newBits.flip(0, mLength);

			//Search for cleared bits
			int index = mBits.nextClearBit(0);
			while ( index >=0 )
			{
				if (checkIndex(index - bits))
				{
					newBits.clear(index - bits);
				}

				index = mBits.nextClearBit(index + 1);
			}

		}

		//Done!
		mBits = newBits;
	}

	public void shiftLeft()
	{
		shiftLeft(1);
	}

	public void shiftLeft(int bits)
	{
		//I intentionally designed the right-shift so that it's reversible
		shiftRight(-bits);
	}

	public void rotRight()
	{
		rotRight(1);
	}

	public void rotRight(int bits)
	{
		//Save the rightmost bits
		BitSet temp = mBits.get(0,  bits);

		//Shift right
		shiftRight(bits);

		//Copy the saved bits back in on the left
		for (int i=0; i<bits; i++)
		{
			if (temp.get(i))
			{
				mBits.set(mLength-bits+i);
			}
		}

	}

	public void rotLeft()
	{
		rotLeft(1);
	}

	public void rotLeft(int bits)
	{
		//Save the leftmost bits
		BitSet temp = mBits.get(mLength-bits,  mLength);

		//Shift right
		shiftLeft(bits);

		//Copy the saved bits back in on the right
		for (int i=0; i<bits; i++)
		{
			if (temp.get(i))
			{
				mBits.set(i);
			}
		}
	}

	//Set all of the bits in the BitBlock to random values
	public void randomize()
	{
		Random r = new Random();
		
		for (int i=0; i<mLength; i++)
		{
			if (r.nextDouble()>0.5)
			{
				set(i);
			}
			else
			{
				clear(i);
			}
		}
	}

	//Miscellaneous methods

	//Check if a particular index is valid; throw an exception if it is not
	private void validateIndex(int index)
	{
		if (!checkIndex(index))
		{
			throw new IndexOutOfBoundsException();
		}
	}

	//Check if a particular index is valid
	private boolean checkIndex(int index)
	{
		if ((index<0)||(index>=mLength))
		{
			return false;
		}

		return true;
	}

	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof BitString)) return false;

		BitString src = (BitString)obj;

		//Note that the BitSet .equals() method ignores size; it only compares set bits
		return ((mLength==src.mLength)&&(mBits.equals(src.mBits)));
	}

	public int hashCode()
	{
		return mBits.hashCode() ^ mLength;
	}

	public String toString()
	{
		String result = "";
		for (int i=0; i<mBits.length(); i++)
		{
			if (mBits.get(i))
			{
				result += "1";
			}
			else
			{
				result += "0";
			}
		}

		return result;
	}

	//Create a BitBlock from a text String, using default encoding
	public static BitString valueOfTextString(String val, int bitsPerCharacter)
	{
		//Create a long array containing all of the characters from the String
		long[] chars = new long[val.length()];
		for (int i=0; i < chars.length; i++)
		{
			chars[i] = val.charAt(i);
		}

		return new BitString(chars, bitsPerCharacter);
	}

	public static BitString valueOfTextString(String val)
	{
		return valueOfTextString(val, Character.SIZE);
	}

	//Create a BitBlock from a String of Hex digits
	public static BitString valueOfHexString(String val)
	{
		//Fix up the text
		String hexString = val.trim().toUpperCase();


		//If the first or second character is an x, skip past it (e.g. 0xFF)
		int xLocation = hexString.indexOf('x');
		if ((xLocation==0)||(xLocation==1))
		{
			hexString = hexString.substring(xLocation+1,  hexString.length());
		}

		//Loop through the HexString, building an array of values
		//Any problems and we return null
		long[] values = new long[hexString.length()];

		for (int i = 0 ; i<values.length; i++)
		{
			char hexDigit = hexString.charAt(i);

			if ((hexDigit>='0')&&(hexDigit<='9'))
			{
				values[i] = hexDigit-'0';
			}
			else if ((hexDigit>='A')&&(hexDigit<='F'))
			{
				values[i] = hexDigit - 'A' + 10;
			}
			else
			{
				return null;
			}
		}

		//Return this array of 4-bit values
		return new BitString(values, 4);
	}

	//Convert BitBlock to a String of Hex digits
	public String toHexString()
	{
		String result = "";
		
		//Loop through all of the "nibbles" in the BitBlock
		for (int index = 0; index<=mLength-4; index += 4)
		{
			//Get the nibble
			long nibble = toLong(index, index+4);
			
			if (nibble <10)
			{
				result += (char)(nibble + '0');
			}
			else
			{
				result += (char)(nibble -10 + 'A');
			}
		}

		return result;		
	}

	//Convert BitBlock to a Text String
	public String toTextString()
	{
		return toTextString(Character.SIZE);
	}
	
	public String toTextString(int bitsPerCharacter)
	{
		String result = "";

		//Loop through all of the characters in the BitBlock
		for (int index = 0; index < mLength; index += bitsPerCharacter)
		{
			result += (char)toLong(index, index+bitsPerCharacter);
		}

		return result;
	}

	//Get a subset of the bits as a long value
	//Convert the first 64 bits (if available) to a long
	public long toLong()
	{
		return toLong(0);
	}

	//Convert the first 64 bits starting from the given index (if available) to a long
	public long toLong(int from)
	{
		return toLong(from, from + Long.SIZE);
	}
	
	//Convert the specified range of bits to a long
	//If the range is longer than 64 bits, only 64 bits will be used
	//Note that starting index is inclusive, ending is exclusive
	public long toLong(int from, int to)
	{
		//Get the relevant subrange
		BitSet temp = new BitSet();
		
		//Reverse the bits
		for (int i=from; i<to; i++)
		{
			if (mBits.get(i))
			{
				temp.set(to-1 + from - i);
			}
		}
		
		long[] value = temp.get(from,to).toLongArray();
		
		if ((value != null)&&(value.length>0))
		{
			return value[0];
		}
		
		return 0;
	}
	
	//Basically just wrappers on BitSet methods to get an array of values
	public long[] toLongArray()
	{
		return mBits.toLongArray();
	}

	public byte[] toByteArray()
	{
		return mBits.toByteArray();
	}
	
}
