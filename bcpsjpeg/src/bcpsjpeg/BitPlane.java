package bcpsjpeg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class BitPlane {
	private static final int MAX_SEGMENT_CHANGES = 64;
	
	private boolean[][] plane;
	private int segmentRow;
	private int segmentColumn;
	
	public BitPlane(int h, int w){
		plane = new boolean[h][w];
		segmentRow = 0;
		segmentColumn = 0;
	}
	
	public void setBit(int row, int col, boolean b){
		plane[row][col] = b;
	}
	
	public boolean getBit(int row, int col){
		return plane[row][col];
	}
	
	public void printPlane(){
		for (int row = 0; row < plane.length; row++){
			for (int col = 0 ; col  < plane[0].length; col++){
				int val =  plane[row][col]? 1 : 0;
				System.out.print(val);
			}
		}
	}
	
	public boolean[] planeToBoolArray(){
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
	
	public float complexity(){
		return (rowComplexity() + colComplexity())/maxChanges();
	}
	
	private int rowComplexity(){
		int sum = 0;
		boolean prev = plane[0][0];
		for (int row = 0; row <plane.length; row ++){
			for (int col = 0; col < plane[0].length; col ++){
				if (prev != plane[row][col]){
					sum++;
					prev= plane[row][col];
				}
			}
		}
		return sum;
	}
	
	private int colComplexity(){
		int sum = 0;
		boolean prev = plane[0][0];
		for (int col = 0; col <plane[0].length; col ++){
			for (int row = 0; row < plane.length; row ++){
				if (prev != plane[row][col]){
					sum++;
					prev= plane[row][col];
				}
			}
		}
		return sum;
	}
	
	private int axisChanges(int x, int y){
		if (x%2==0)
			return x/2*y;
		else
			return (x/2+1)*y;
	}
	
	private int maxChanges(){
		if (plane.length == 8 && plane[0].length == 8) return MAX_SEGMENT_CHANGES;
		return axisChanges(plane.length, plane[0].length) + axisChanges(plane[0].length, plane.length);
	}
	
	public boolean hasNextSegment(){
		int maxW = getWidth() - getWidth()%8;
		int maxH = getHeight() - getHeight()%8;
		
		if (maxW-9 >= segmentColumn) return true;
		if (maxH-9 >= segmentRow) return true;
		
		return false;
	}
	
	public BitPlane getNextSegment(){
		BitPlane seg = new BitPlane(8,8);
		int maxW = getWidth() - getWidth()%8;
		int maxH = getHeight() - getHeight()%8;
		
		if (maxW-9 < segmentColumn) 
			segmentColumn = 0;
		else 
			segmentColumn +=8;
		
		if (maxH-9 < segmentRow) 
			segmentRow = 0;
		else
			segmentRow += 8;
		
		
		for (int x = 0; x < 8; x ++)
			for (int y = 0; y < 8; y++)
				seg.setBit(x,y,getBit(segmentRow+x, segmentColumn+y));
		
		return seg;
	}
}
