package bcpsjpeg;

public class GrayCode {
	public static int convertToGray(int number){
		return  number ^ number >>> 1;
	}
	
}
