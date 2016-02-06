package pcp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;

public class CardReader implements Runnable {
	
	private class PcpPixel {
		
	}
	
	private static final String JPG = "image/jpeg";
	public Path filepath;

	public CardReader(Path filepath) {
		this.filepath = filepath;
	}
	
	@Override
	public void run() {
		String fileType;
		try {
			fileType = Files.probeContentType(filepath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.format("File type is: %s \n", fileType);
		
		if(JPG.equals(fileType)) {
			try {
		        BufferedImage originalImage = ImageIO.read(filepath.toFile());

		        final byte[] pixels = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
		        final int width = originalImage.getWidth();
		        final int height = originalImage.getHeight();		        
		        
		        int[][] result = new int[height][width];
		        final int pixelLength = 3;
		         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
		            int argb = 0;
		            argb += -16777216; // 255 alpha
		            argb += ((int) pixels[pixel] & 0xff); // blue
		            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
		            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
		            result[row][col] = argb;
		            System.out.println(argb);
		            col++;
		            if (col == width) {
		               col = 0;
		               row++;
		            }
		         }
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}

	}
	
}
