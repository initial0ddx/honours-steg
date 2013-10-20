
package bcpsjpeg;

import javax.imageio.ImageIO;

import java.awt.image.PixelGrabber;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
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
	    	planes[i].planeToImage("plane" + i);
	    }
	    
	    System.out.println("Completed plane division");
	    
	    boolean[][] wc = new boolean[8][8]; //wc as defined by Kawaguchi 1986
	    boolean prev = false;
	    for (int x = 1; x < 8; x++)
	    	for(int y = 0; y < 8; y ++){
	    		wc[x][y] = !prev;
	    		prev = !prev;
	    	}
	    
	    String dataFile = "message.txt";
	    File file = new File(dataFile);
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFile)));
	    
		boolean[] data = new boolean[(int)file.length()*8];
		
		//turns file into bits
		try{
			while(true){
				byte b;
				for (int i = 0; i < data.length; i++){
					b = in.readByte();
					for (int j = 0; j < 8; j++){
						data[i*8+j] = (b&(1<<j)) != 0;
					}
				}
			}
		} catch (EOFException e){
		}
		
		//TODO split file into segments and calc complexity.
	    for (int i = 0; i < planes.length; i++){
	    	int count = 0;
	    	while (planes[i].hasNextSegment()){
	    		BitPlane seg = planes[i].getNextSegment();
	    		System.out.println(i + "-"+count+": "+seg.complexity());
	    		seg.planeToImage("segment"+i+"-"+count);
	    		if (seg.complexity() >= COMPLEXITY_THRESHOLD) System.out.println("Segment: "+count + "for plane: " +i + "is complex");
	    		count++;
	    	}
	    }
	    
	}
	

}
