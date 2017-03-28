package ksk.math;

import java.util.BitSet;

/*
 * A BitBlock is like a BitSet, but with size that is fixed from the moment of construction.
 * BitSets grow or shrink based on the highest bit set.
 * 
 * Although BitBlock will contain methods that are almost identical to those in BitSet,
 * it does not extend BitSet; it implements only a subset of the BitSet methods, and only
 * with other BitBlocks as parameters.
 * 
 * Rather than extending BitSet, BitBlock will maintain a BitSet field.
 * (HAS-A BitSet, not IS-A BitSet)
 * 
 */

public class BitBlock {
	private int mLength;
	private BitSet mBits;

	//Constructor:  Zero BitBLock
	public BitBlock(int length)
	{
		this(null, length);
	}

	//Constructor:  BitBlock containing a long value
	public BitBlock(long val, int length)
	{
		this(new long[]{val}, length);
	}

	//Constructor:  BitBlock containing all of the bits from a little-endian long array (overflow bits are ignored)
	public BitBlock(long[] values, int bitsPerValue)
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
					//Set the current bit
					mBits.set(mBitIndex, values[arrayIndex]%2==1);

					//Shift the current value
					values[arrayIndex] /= 2;

					//Shift the mBitIndex
					mBitIndex++;
				}
			}
		}
	}

	public BitBlock(long[] values)
	{
		this(values, Long.SIZE);
	}

	//Constructor:  Copy
	public BitBlock(BitBlock src)
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

	public BitBlock get(int from,int to)
	{
		validateIndex(from);
		validateIndex(to-1);
		BitSet bitSetRange = mBits.get(from, to);
		BitBlock bitBlockRange = new BitBlock(to-from+1);
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
	boolean isEmpty()
	{
		return mBits.isEmpty();
	}

	boolean intersects(BitBlock block)
	{
		return mBits.intersects(block.mBits);
	}

	void xor(BitBlock block)
	{
		mBits.xor(block.mBits);
	}

	void and(BitBlock block)
	{
		mBits.andNot(block.mBits);
	}

	void or(BitBlock block)
	{
		mBits.or(block.mBits);
	}

	void not()
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
		if (!(obj instanceof BitBlock)) return false;

		BitBlock src = (BitBlock)obj;

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
		for (int i=mLength-1; i>=0; i--)
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
	public static BitBlock valueOfTextString(String val, int bitsPerCharacter)
	{
		//Create a long array containing all of the characters from the String
		long[] chars = new long[val.length()];
		for (int i=0; i<chars.length; i++)
		{
			chars[i] = val.charAt(i);
		}

		return new BitBlock(chars, bitsPerCharacter);
	}

	public static BitBlock valueOfTextString(String val)
	{
		return valueOfTextString(val, Character.SIZE);
	}

	//Create a BitBlock from a String of Hex digits
	public static BitBlock valueOfHexString(String val)
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
		return new BitBlock(values, 4);
	}

	//Convert BitBlock to a String of Hex digits
	public String toHexString()
	{
		//Get an array of Bytes
		byte[] bytes = mBits.toByteArray();

		String result = "";
		for (int i=0; i<bytes.length; i++)
		{
			//Two digits per byte
			for (int nib=0; nib<2; nib++)
			{
				int digit = bytes[i]&0x0F;

				if ((digit>=0)&&(digit<=9))
				{
					result += (digit + '0');
				}
				else if ((digit>=10)&&(digit<=15))
				{
					result += (digit - 10 + 'a');
				}
				else
				{
					result += '-';
				}
			}
			
			//Shift the byte
			bytes[i] >>= 4;
		}

		return result;		
	}

	//Convert BitBlock to a Text String
	public String toTextString()
	{
		//Get an array of Bytes
		byte[] bytes = mBits.toByteArray();

		String result = "";
		for (int i=0; i<bytes.length; i++)
		{
			result += (char)bytes[i];
		}

		return result;
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
