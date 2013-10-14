package bcpsjpeg;

import javax.imageio.ImageIO;

import java.awt.image.PixelGrabber;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;


public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedImage img = ImageIO.read(Main.class.getResourceAsStream("facebook small.bmp"));
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
	    for (int j = 0; j < h; j++) { //for each row
	    	for (int i = 0; i < w; i++) { //for each column
	    		
	    		//int cgc = GrayCode.convertToGray(pixels[j*w+i]); //generates the gray code equivalent of the pixel
	    		int cgc = pixels[j*w+i]; //the current pixel
	    		System.out.println(cgc);
	    		byte[] bytes = new byte[3]; //will hold the 3 bits of a 24bit image
	    		for (int y = 0; y < 3; y++) { 
	    			bytes[y] = (byte)(cgc >>> (y * 8));//a byte array representation of the pixel in CGC format
	    		}
	    		boolean[] bits = new boolean[24];
	    		for (int k = 23; k>=0;k--){
	    			bits[k] = (cgc & (1<<k)) !=0;
	    		}
	    		System.out.println(bits);
	    		
	    		for (int x = 0; x < 24; x++){//add pixel (CGC format) data to bit planes.
	    			planes[x].setBit(i, j, bits[x]);
	    		}
	    	}
	    }
	    
	    planes[0].printPlane();
	    System.out.println();
	    planes[1].printPlane();
	    
	    for (int i = 0; i<24; i++){
	    	BufferedImage planeImage =  planes[i].planeToImage();
	    	ImageIO.write(planeImage, "bmp", new File("test"+i+".bmp"));
	    }
	    
	    System.out.println("Completed plane division");

	    
	}
	

}
