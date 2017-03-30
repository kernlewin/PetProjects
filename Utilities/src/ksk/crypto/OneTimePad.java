package ksk.crypto;

import ksk.math.BitBlock;

//Represents a One-Time pad cipher
//XORs key and message together for perfect security, IF the pad is random, and used only once.

public class OneTimePad extends Cipher {

	public OneTimePad(BitBlock key)
	{
		super(key);
	}
	
	@Override
	public BitBlock Encode(BitBlock key, BitBlock message) {
		
		BitBlock cipherText = new BitBlock(message);
		cipherText.xor(key);
		
		return cipherText;
	}

	@Override
	public BitBlock Decode(BitBlock key, BitBlock cipherText) {
		BitBlock message = new BitBlock(cipherText);
		message.xor(key);
		
		return message;
	}
}
