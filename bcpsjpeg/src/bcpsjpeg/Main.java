
package bcpsjpeg;

import javax.imageio.ImageIO;

import java.awt.image.PixelGrabber;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedImage img = ImageIO.read(Main.class.getResourceAsStream("lena512.bmp"));
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
	    

	    
	    String dataFile = "message.txt";
	    Payload payload = new Payload(dataFile);
	    
	    int paycount = 0;
	    int planecount =0;
	    while (payload.hasNextSegment()){
	    	BitPlane seg = payload.nextSegment();
	    	System.out.println ("Payload segment "+ paycount++ +"'s compexity == "+seg.complexity());
	    	
	    	if (seg.complexity() < BitPlane.COMPLEXITY_THRESHOLD) {
	    		seg.planeToImage("payload-nc-"+paycount);
	    		seg.conjugate();
	    		seg.setBit(7,7,true);
	    		seg.planeToImage("payload-c-"+paycount);
	    	} else
	    		seg.setBit(7, 7, false);
	    	
	    	while(true){
	    		boolean foundNoisySeg = planes[planecount].findNoisySegment();
		    	if (foundNoisySeg) {
		    		planes[planecount].writeToCurrentSeg(seg);
		    		break;
		    	}
		    	planecount++;
		    	if (planecount == 24){
		    		System.out.println("Not enough space in image for payload storage");
		    		return;
		    	}
	    	}
	    	
	    }
	    
	    System.out.println("Succesfully hidden message");
	    System.out.println("img size: "+ (h*w));
	    byte[] bytes = new byte[h*w*3];
	    int byteCount = 0;
	    for (int row = 0; row < h ; row ++)
	    	for (int col = 0; col < w; col ++) {
	    
			    for (int i = 0,  j = 0,  k = 7; i < 24; i++ ){
			    	//System.out.println("bytecount+j: " + (byteCount+j));
			    	bytes[byteCount+j]  |=( planes[i].getBit(row, col) ? 1:0 )<< k--;
			    	if (k < 0) { j++; k = 7;}
			    }
			    byteCount +=3;
	    	}
	    
	    System.out.println("Succesfully created byte stream");

	    BufferedImage stegImg = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
	    
	    //TODO bytes[1] should map to the 3 bytes which make up each pixel.
	    byteCount = 0;
	    for (int row = 0; row < h ; row ++)
	    	for (int col = 0; col < w; col ++) {
	    		//int r = ((bytes[byteCount+2] & 0xFF) | ((bytes[byteCount+1] & 0xFF) << 8) | ((bytes[byteCount] & 0x0F) << 16));
	    		//int r = ((bytes[byteCount+2] ) | ((bytes[byteCount+1] ) << 8) | ((bytes[byteCount] ) << 16));
	    		int r = (((bytes[byteCount] ) << 16) |  ((bytes[byteCount+1] ) << 8) | (bytes[byteCount+2] ) );


	    		r = GrayCode.convertToBin(r);
	    		stegImg.setRGB(col, row, r);
	    		byteCount+=3;
	    	}
	    		ImageIO.write(stegImg, "bmp", new File("secret.bmp"));
		
	    System.out.println("Succesfully saved stegimg");

	}
	

}
