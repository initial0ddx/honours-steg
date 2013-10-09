package bcpsjpeg;

import static org.junit.Assert.*;

import org.junit.Test;

public class GrayCodeTest {

	@Test
	public void testConvertToGray() {
		int expected = 0b10010100;
		int test = GrayCode.convertToGray(231);
		assertEquals("fail to convert: received " + Integer.toBinaryString(test) + " for 3. sent: "+ Integer.toBinaryString(3), expected, test);
	}
	

}
