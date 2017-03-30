package ksk.crypto;

import ksk.math.BitBlock;

/*
 * Represents a generic cipher
 * 
 * A cipher is must contain an Encode function that takes a Key and a Message to produce a Ciphertext,
 * such that the Decode function will produce the same message, given an appropriate Key and the same Ciphertext
 */

public abstract class Cipher {
	
	protected BitBlock mKey;
	
	//Keyed constructor
	public Cipher(BitBlock key)
	{
		mKey = key;
	}
	
	//For Ciphers without a persistent key
	public Cipher()
	{
		mKey = null;
	}
	
	public abstract BitBlock Encode (BitBlock key, BitBlock message);
	
	public abstract BitBlock Decode (BitBlock key, BitBlock cipherText);
	
	//For Ciphers that use the same key throughout their lifetime
	public BitBlock Encode(BitBlock message)
	{
		return Encode(mKey, message);
	}
	
	public BitBlock Decode(BitBlock cipherText)
	{
		return Decode(mKey, cipherText);
	}
}
