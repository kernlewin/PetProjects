import java.util.Scanner;

import ksk.crypto.OneTimePad;
import ksk.math.BitBlock;


public class BitBlockTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		// TODO Auto-generated method stub
		
		/*BitBlock block1 = new BitBlock(3, 8);
		BitBlock block2 = new BitBlock(20, 8);
		
		block1.shiftLeft(2);
		block2.rotRight(4);
		
		System.out.println(block1);
		System.out.println(block2);
		
		block1.setLength(4);
		block2.setLength(4);
		
		System.out.println(block1);
		System.out.println(block2);

		block1.setLength(12);
		block2.setLength(12);
		
		System.out.println(block1);
		System.out.println(block2);
		*/
		System.out.print("Please type some text: ");
		String text = sc.nextLine();
		
		BitBlock bits = BitBlock.valueOfTextString(text, 8);
		
		//Show in binary
		System.out.println(bits);
		
		//Back to text
		System.out.println(bits.toTextString(8));
		//Show in Hex
		String hex = bits.toHexString();
		System.out.println(hex);
		
		//Convert Hex back to text
		System.out.println(BitBlock.valueOfHexString(hex).toTextString(8));
		
		
		
		System.out.print("Please type some text: ");
		text = sc.nextLine();
		
		BitBlock message = BitBlock.valueOfTextString(text,8);
		BitBlock key = new BitBlock(message.getLength());
	key.randomize();
		
		//Create the OneTimePad Cipher
		OneTimePad otp = new OneTimePad(key);
		
		BitBlock cipherText = otp.Encode(message);
		
		System.out.println("Message = " + message);
		System.out.println("Key     = " + key);
		System.out.println("Cipher  = " + cipherText);
		
		//To make sure that some of the conversion methods are working, create a new Cipher that should have the same key
		//if all conversions are correct
		//key = BitBlock.valueOfHexString(key.toHexString());
		//otp = new OneTimePad(key);
		
		message = otp.Decode(cipherText);
		System.out.println("Decoded = " + message);
		System.out.println(message.toTextString(8));
		
		
		/*
		BitBlock cipher = BitBlock.valueOfHexString("6c73d5240a948c86981bc294814d");
		BitBlock message = BitBlock.valueOfTextString("attack at dawn",8);
		BitBlock key = new BitBlock(cipher);
		key.xor(message);
		
		BitBlock newCipher = BitBlock.valueOfTextString("attack at dusk",8);
		newCipher.xor(key);
		System.out.println(newCipher.toHexString().toLowerCase());
		*/
	}

}
