package bcpsjpeg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class BitPlane {
	private boolean[][] plane;
	
	public BitPlane(int w, int h){
		plane = new boolean[w][h];
	}
	
	public void setBit(int row, int col, boolean b){
		plane[row][col] = b;
	}
	
	public void printPlane(){
		for (int i = 0; i < plane.length; i++){
			for (int j = 0 ; j  < plane[0].length; j++){
				int val =  plane[i][j]? 1 : 0;
				System.out.print(val);
			}
		}
	}
	
	public byte[] planeToByteArray(){
		int size = plane.length*plane[0].length;
		byte[] bytes = new byte[size];
		int count = 0;
		for (int i = 0; i < plane.length; i++){
			for (int j = 0; j < plane[0].length; j++){
				bytes[count] = (byte) (plane[i][j]? 1:0);
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
	
}
