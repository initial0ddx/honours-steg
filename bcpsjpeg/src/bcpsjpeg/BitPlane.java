package bcpsjpeg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class BitPlane {
	private boolean[][] plane;
	
	public BitPlane(int h, int w){
		plane = new boolean[h][w];
	}
	
	public void setBit(int row, int col, boolean b){
		plane[row][col] = b;
	}
	
	public void printPlane(){
		for (int row = 0; row < plane.length; row++){
			for (int col = 0 ; col  < plane[0].length; col++){
				int val =  plane[row][col]? 1 : 0;
				System.out.print(val);
			}
		}
	}
	
	public boolean[] plantToBoolArray(){
		boolean[] b = new boolean[plane.length*plane[0].length];
		for (int row = 0; row < plane.length; row++)
			for (int col = 0; col < plane[0].length; col++){
				b[row*plane[0].length+col] = plane[row][col];
			}
		return b;
	}
		
	
	public byte[] planeToByteArray(){
		int size = plane.length*plane[0].length;
		byte[] bytes = new byte[size];
		int count = 0;
		for (int row = 0; row < plane.length; row++){
			for (int col = 0; col < plane[0].length; col++){
				bytes[count] = (byte) (plane[row][col]? 1:0);
				count++;
			}
		}

		return bytes;
	}
	
	public BufferedImage planeToImage() throws IOException{
		byte[] bytes = planeToByteArray();
		System.out.println();
		for (int i = 0; i <bytes.length ; i++){
			System.out.print(bytes[i]);
		}
		System.out.println();
		InputStream stream = new ByteArrayInputStream(bytes);
		BufferedImage img = ImageIO.read(stream);
		ImageIO.write(img, "bmp", new File("test.bmp"));
		return img;

	}
	
	public int[] planeToIntArray(){
		int [] pixels = new int[plane.length*plane[0].length];
		for (int row = 0; row < plane.length; row++)
			for (int col = 0; col < plane[0].length; col++)
				pixels[row*plane[0].length+col] = plane[row][col]? 1:0;
		return pixels;
	}
	
	public int getWidth(){
		return plane[0].length;
	}
	
	public int getHeight(){
		return plane.length;
	}
}
