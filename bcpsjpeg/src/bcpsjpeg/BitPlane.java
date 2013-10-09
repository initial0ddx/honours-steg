package bcpsjpeg;

public class BitPlane {
	private boolean[][] plane;
	
	public BitPlane(int w, int h){
		plane = new boolean[w][h];
	}
	
	public void setBit(int row, int col, boolean b){
		plane[col][row] = b;
	}
	
	
}
