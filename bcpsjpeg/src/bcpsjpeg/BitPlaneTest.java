/**
 * 
 */
package bcpsjpeg;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ar
 *
 */
public class BitPlaneTest {

	private BitPlane plane;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		plane = new BitPlane(3,3);
		for (int x = 0; x < 3; x++)
			for (int y = 0; y<3; y++)
				plane.setBit(x,y,true);
		plane.setBit(1,1,false);
	}

	/**
	 * Test method for {@link bcpsjpeg.BitPlane#planeToBoolArray()}.
	 */
	@Test
	public void testPlaneToBoolArray() {
		boolean[] b = {true, true, true, true, false, true, true, true, true};
		boolean[] planeB = plane.planeToBoolArray();
		
		for (int x = 0; x < b.length; x++)
			assertEquals("item "+x+ "not equal", b[x], planeB[x]);
		
	}

	/**
	 * Test method for {@link bcpsjpeg.BitPlane#planeComplexity()}.
	 */
	@Test
	public void testPlaneComplexity() {
		float knownComplexity = 4/12;
		assertEquals("complexity wrong", knownComplexity, plane.complexity(), 0);
	}

}
