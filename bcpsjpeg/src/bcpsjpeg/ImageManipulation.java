package bcpsjpeg;

import javax.imageio.ImageIO;

import java.awt.image.PixelGrabber;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageManipulation {
	
	public BufferedImage getImage() throws IOException{
		BufferedImage img = ImageIO.read(getClass().getResourceAsStream(""));
		return img;
	}
	
	
}
