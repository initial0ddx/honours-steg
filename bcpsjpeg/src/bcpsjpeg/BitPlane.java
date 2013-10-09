package bcpsjpeg;

public class BitPlane {
	private boolean[][] plane;
	
	public BitPlane(int w, int h){
		plane = new boolean[w][h];
	}
	
	public void setBit(int row, int col, boolean b){
		plane[col][row] = b;
	}
	
	public void printPlane(){
		for (int i = 0; i < plane.length; i++){
			for (int j = 0 ; j  < plane[0].length; j++){
				int val =  plane[i][j]? 1 : 0;
				System.out.print(val);
			}
		}
	}
	
	public void planeToByteArray(){
		int size = plane.length*plane[0].length;
		byte[] bytes = new byte[(size+7)/8];
		for (int i = 0; i < plane.length; i++){
			for (int j = 0; i < plane[0].length; i++){
				if (plane[i][j])
					bytes[bytes.length - (i*j)/8-1] |= 1 << ((i*j) %8);
			}
		}
	}
	
}
