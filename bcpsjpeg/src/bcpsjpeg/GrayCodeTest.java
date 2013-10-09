package bcpsjpeg;

import static org.junit.Assert.*;

import org.junit.Test;

public class GrayCodeTest {

	@Test
	public void testConvertToGray() {
		int expected = 0b10010100;
		int test = GrayCode.convertToGray(0b11100111);
		assertEquals("fail to convert: received " + Integer.toBinaryString(test) + " for 11100111. sent: "+ Integer.toBinaryString(3), expected, test);
	}
	
	@Test
	public void testConvertToBin(){
		int expected = 0b11100111;
		int test = GrayCode.convertToBin(0b10010100);
		assertEquals("fail to convert: received " + Integer.toBinaryString(test) + " for 0b10010100. sent: "+ Integer.toBinaryString(3), expected, test);

	}
	

}
