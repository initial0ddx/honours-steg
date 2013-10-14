package bcpsjpeg;


public class GrayCode {
	
	//convert pure binary code to gray code
	public static int convertToGray(int number){
		return  number ^ number >>> 1;
	}
	
	
	//convert gray code to pure binary code
	public static int convertToBin(int gray){
		int num  = 0;
		while (gray !=0){
				num ^= gray; // exclusive or
				gray >>>= 1; // bit shift right by 1
		}
		return num;
	}
	
}
