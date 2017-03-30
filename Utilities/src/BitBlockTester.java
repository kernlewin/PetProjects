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
		/*
		BitBlock block1 = new BitBlock(3, 8);
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
		
		System.out.print("Please type some text: ");
		String text = sc.next();
		
		BitBlock bits = BitBlock.valueOfTextString(text);
		
		//Show in binary
		System.out.println(bits);
		
		//Back to text
		System.out.println(bits.toTextString());
		//Show in Hex
		String hex = bits.toHexString();
		System.out.println(hex);
		
		//Convert Hex back to text
		System.out.println(BitBlock.valueOfHexString(hex).toTextString());
		*/
		
		/*
		System.out.print("Please type some text: ");
		String text = sc.next();
		
		BitBlock message = BitBlock.valueOfTextString(text);
		BitBlock key = new BitBlock(message.getLength());
	key.randomize();

		//Create the OneTimePad Cipher
		OneTimePad otp = new OneTimePad(key);
		
		BitBlock cipherText = otp.Encode(message);
		
		System.out.println("Message = " + message);
		System.out.println("Key     = " + key);
		System.out.println("Cipher  = " + cipherText);
		message = otp.Decode(cipherText);
		System.out.println("Decoded = " + message);
		System.out.println(message.toTextString());
		*/
		
		BitBlock cipher = BitBlock.valueOfHexString("6c73d5240a948c86981bc294814d");
		BitBlock message = BitBlock.valueOfTextString("attack at dawn",8);
		BitBlock key = new BitBlock(cipher);
		key.xor(message);
		
		BitBlock newCipher = BitBlock.valueOfTextString("attack at dusk",8);
		newCipher.xor(key);
		System.out.println(newCipher.toHexString().toLowerCase());
	}

}
