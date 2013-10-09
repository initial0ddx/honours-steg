package bcpsjpeg;

import javax.imageio.ImageIO;

import java.awt.image.PixelGrabber;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.BitSet;


public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedImage img = ImageIO.read(Main.class.getResourceAsStream("lena512.bmp"));
		int w = img.getWidth();
		int h = img.getHeight();
		
		BitPlane[] planes = new BitPlane[24]; //for 24bit images
		for (int i = 0; i < 24 ; i++)
			planes[i] = new BitPlane(w,h);
		
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
	    for (int j = 0; j < h; j++) {
	    	for (int i = 0; i < w; i++) {
	    		
	    		//int cgc = GrayCode.convertToGray(pixels[j*w+i]); //generates the gray code equivalent of the pixel
	    		int cgc = pixels[j*w+i];
	    		byte[] bytes = new byte[3];
	    		for (int y = 0; y < 3; y++) {
	    			bytes[y] = (byte)(cgc >>> (y * 8));//a byte array representation of the pixel in CGC format
	    		}
	    		BitSet bits = BitSet.valueOf(bytes);
	    		
	    		for (int x = 0; x < 24; x++){//add pixel (CGC format) data to bit planes.
	    			planes[x].setBit(i, j, bits.get(x));
	    		}
	    	}
	    }
	    
	    System.out.println("Completed plane division");
	    for (int x = 0; x < 24; x++){
	    	System.out.println("plane: "+x);
	    	planes[x].printPlane();
	    }
	    
	}
	

}
