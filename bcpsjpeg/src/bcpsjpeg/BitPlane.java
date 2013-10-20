package bcpsjpeg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class BitPlane {
	private static final int MAX_SEGMENT_CHANGES = 64; //the number of changes (b+w border) in an 8x8 segment
	
	private boolean[][] plane; 	//2d array to hold the bits as booleans
	private int segmentRow; 	//holds the starting row of the current segment
	private int segmentColumn;	//holds the starting column of the current segment
	
	
	/**
	 * basic constructor
	 * 
	 * @param h the height of the image to be represented
	 * @param w the width of the image to be represented
	 */
	public BitPlane(int h, int w){
		plane = new boolean[h][w];
		segmentRow = 0;
		segmentColumn = 0;
	}
	
	
	/**
	 * sets a bit in the plane
	 * 
	 * @param row the y coordinate of the pixel
	 * @param col the x coordinate of the pixel
	 * @param b the bit to be stored
	 */
	public void setBit(int row, int col, boolean b){
		plane[row][col] = b;
	}
	
	/**
	 * retrieves the requested bit as a boolean value
	 * 
	 * @param row the y coordinate of the pixel
	 * @param col the x coordinate of the pixel
	 * @return boolean representation of bit
	 */
	public boolean getBit(int row, int col){
		return plane[row][col];
	}
	
	
	/**
	 *prints the plane to standard output 
	 */
	public void printPlane(){
		for (int row = 0; row < plane.length; row++){
			for (int col = 0 ; col  < plane[0].length; col++){
				int val =  plane[row][col]? 1 : 0;
				System.out.print(val + " ");
			}
		}
	}
	
	/**
	 * returns the plane as a 1D boolean array
	 * @return a 1D boolean array representation of the plane
	 */
	public boolean[] planeToBoolArray(){
		boolean[] b = new boolean[plane.length*plane[0].length];
		for (int row = 0; row < plane.length; row++)
			for (int col = 0; col < plane[0].length; col++){
				b[row*plane[0].length+col] = plane[row][col];
			}
		return b;
	}
		
	
	/**
	 * returns the plane as a 1D byte array
	 * @return a 1D byte array representation of the plane. Each pixel is represented as 1 byte
	 */
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
	
	
	/**
	 * writes the plane to a BMP as binary image 
	 * @param name the name of the ouput file
	 * @throws IOException
	 */
	public void planeToImage(String name) throws IOException{
    	boolean[] b = this.planeToBoolArray();
    	BufferedImage planeImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_BINARY);
    	for (int row = 0; row < getHeight(); row++)
			for (int col = 0; col <getWidth(); col++){
				int val = b[row*getWidth()+col]? 0:16777215;
				planeImage.setRGB(col,row,val);
			}
       	ImageIO.write(planeImage, "bmp", new File(name+ ".bmp"));
	}
	
	/**
	 * returns the plane as a 1D array of integers
	 * @return 1D array of integers
	 */
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
	
	/**
	 * complexity calculation based on black and white border complexity
	 * @return float between 0 and 1 representing the complexity of the plane
	 */
	public float complexity(){
		return (float)(rowComplexity() + colComplexity())/maxChanges();
	}
	
	/**
	 * @return total number of b->w and w->b changes in the rows of the image
	 */
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
	
	/**
	 * @return total number of b->w and w->b changes in the columns of the plane
	 */
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
	
	/**
	 * Calculates the maximum number of changes along one axis of the plane
	 * 
	 * @param x total number of rows or columns
	 * @param y total number of rows or columns
	 * @return the max number of changes (b+w) along one axis
	 */
	private int axisChanges(int x, int y){
		if (x%2==0)
			return x/2*y;
		else
			return (x/2+1)*y;
	}
	
	/**
	 * @return maximum possible changes (b->w and w-b) in the plane
	 */
	private int maxChanges(){
		if (plane.length == 8 && plane[0].length == 8) return MAX_SEGMENT_CHANGES;
		return axisChanges(plane.length, plane[0].length) + axisChanges(plane[0].length, plane.length);
	}
	
	/**
	 * mandatory test prior to <code>getNextSegment()</code>
	 * 
	 * @return true if next segment present, otherwise false
	 */
	public boolean hasNextSegment(){
		int maxW = getWidth() - getWidth()%8;
		int maxH = getHeight() - getHeight()%8;
		
		if (maxW-9 >= segmentColumn) return true;
		if (maxH-9 >= segmentRow) return true;
		
		return false;
	}
	
	/**
	 * retrieves the next 8x8 segment of the current plane as a new plane
	 * @return BitPlane 8x8 segment
	 */
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
