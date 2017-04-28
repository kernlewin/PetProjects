package UnitTesting;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import ksk.math.BitBlock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class BitBlockTest
{
//********************************
	//   JUnit Tests
	//********************************
	
	public @Rule Timeout timer = new Timeout(1, TimeUnit.SECONDS); //1 second limit
	
	@Test
	public void valueOfTextStringTest1()
	{
		String text = "hello";
		String expected = "00680065006C006C006F";
		BitBlock textBlock = BitBlock.valueOfTextString(text);
		assertEquals(expected, textBlock.toHexString());
	}
	
	@Test
	public void valueOfTextStringTest2()
	{
		String text = "hello";
		String expected = "68656C6C6F";
		BitBlock textBlock = BitBlock.valueOfTextString(text,8);
		assertEquals(expected, textBlock.toHexString());
	}
}	