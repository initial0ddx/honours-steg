package bcpsjpeg;

public class GrayCode {
	public static int convertToGray(int number){
		return  number ^ number >>> 1;
	}
	
	public static int convertToBin(int gray){
		int num  = 0;
		while (gray !=0){
				num ^= gray;
				gray >>>= 1;
		}
		return num;
	}
	
}
