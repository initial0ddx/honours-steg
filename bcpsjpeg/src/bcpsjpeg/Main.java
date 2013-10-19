
package bcpsjpeg;

import javax.imageio.ImageIO;

import java.awt.image.PixelGrabber;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
	public static final double COMPLEXITY_THRESHOLD = 0.1;

	public static void main(String[] args) throws IOException {
		
		BufferedImage img = ImageIO.read(Main.class.getResourceAsStream("facebook small.bmp"));
		int w = img.getWidth();
		int h = img.getHeight();
		
		BitPlane[] planes = new BitPlane[24]; //for 24bit images
		for (int i = 0; i < 24 ; i++)
			planes[i] = new BitPlane(h,w);
		
		System.out.print("image width: " + w + " height: "+h);
		
		
		int[] pixels = new int[h * w];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0 , w);
		
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			System.err.println("interrupted waiting for pixels!");
			return;
		}
		
		
		//divide the image into 24 bit planes
	    for (int row = 0; row < h; row++) { //for each row
	    	for (int col = 0; col < w; col++) { //for each column
	    		
	    		int cgc = GrayCode.convertToGray(pixels[row*w+col]); //generates the gray code equivalent of the pixel
	    		byte[] bytes = new byte[3]; //will hold the 3 bits of a 24bit image
	    		for (int y = 0; y < 3; y++) { 
	    			bytes[y] = (byte)(cgc >>> (y * 8));//a byte array representation of the pixel in CGC format
	    		}
	    		boolean[] bits = new boolean[24];
	    		for (int k = 23; k>=0;k--){
	    			bits[k] = (cgc & (1<<k)) !=0;
	    		}	    		
	    		for (int x = 0; x < 24; x++){//add pixel (CGC format) data to bit planes.
	    			planes[x].setBit(row, col, bits[x]);
	    		}
	    	}
	    }
	    
	    //generates an image per plane
	    for (int i = 0; i<24; i++){
	    	boolean[] b = planes[i].planeToBoolArray();
	    	BufferedImage planeImage = new BufferedImage(planes[i].getWidth(), planes[i].getHeight(), BufferedImage.TYPE_BYTE_BINARY);
	    	for (int row = 0; row < planes[i].getHeight(); row++)
				for (int col = 0; col < planes[i].getWidth(); col++){
					int val = b[row*planes[i].getWidth()+col]? 0:16777215;
					planeImage.setRGB(col,row,val);
				}
	    	
	    	//BufferedImage planeImage =  planes[i].planeToImage();
	    	ImageIO.write(planeImage, "bmp", new File("test"+i+".bmp"));
	    }
	    
	    System.out.println("Completed plane division");
	    
	    for (int i = 0; i < planes.length; i++){
	    	int count = 0;
	    	while (planes[i].hasNextSegment()){
	    		System.out.println("reacjed " + i);
	    		BitPlane seg = planes[i].getNextSegment();
	    		if (seg.complexity() >= COMPLEXITY_THRESHOLD) System.out.println("Segment: "+count + "for plane: " +i + "is complex");
	    	}
	    }
	    
	}
	

}
