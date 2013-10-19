package bcpsjpeg;

public class SegmentedBitPlane {

	private BitPlane[] segments;
	
	public SegmentedBitPlane(BitPlane plane) {
		int divisor = plane.getHeight()>plane.getWidth()? plane.getHeight() : plane.getWidth() ;
		segments = new BitPlane[divisor/8];
		
		//ignore any end rows and cols
		int maxW = plane.getWidth() - plane.getWidth()%8;
		int maxH = plane.getHeight() - plane.getHeight()%8;
		
		
	}
	
	
}
