package bcpsjpeg;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Payload {
	private File file;
	private boolean[] data;
	private int currentIndex;

	public Payload(String name) throws FileNotFoundException{
		file = new File(name);
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(name)));
	    
		data = new boolean[(int)file.length()*8];
		
		//turns file into bits
		try{
			while(true){
				byte b;
				for (int i = 0; i < data.length; i++){
					b = in.readByte();
					for (int j = 0; j < 8; j++){
						data[i*8+j] = (b&(1<<j)) != 0;
					}
				}
			}
		} catch (EOFException e){
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean hasNextSegment(){
		return currentIndex+63 < data.length-1;
	}
	
	public BitPlane nextSegment(){
		BitPlane seg = new BitPlane(8,8);
		currentIndex += 63;
		int gap = hasNextSegment()? 63:data.length-currentIndex-1;
		
		for (int x = 0; x < 8; x ++)
			for (int y = 0; y < 8; y++){
				if (gap == 0) return seg;
				seg.setBit(x,y,data[x*8 + y]);
				gap--;
			}
		
		return seg;
	}
}
