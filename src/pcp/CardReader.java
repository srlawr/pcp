package pcp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;

public class CardReader implements Runnable {

	private static final String JPG = "image/jpeg";
	public Path filepath;

    PcpPixel[][] result;

	public CardReader(Path filepath) {
		this.filepath = filepath;
	}
	
	@Override
	public void run() {
		String fileType;
		try {
			fileType = Files.probeContentType(filepath);
			System.out.format("File type is: %s \n", fileType);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if(!JPG.equals(fileType)) {
			System.out.println("Non-Jpg file format");
			return;
		}
		
		processFile();
		compileCard();
	}
	
	private void processFile() {
		try {
	        BufferedImage originalImage = ImageIO.read(filepath.toFile());

	        final byte[] pixels = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
	        final int width = originalImage.getWidth();
	        final int height = originalImage.getHeight();
	        
	        result = new PcpPixel[height][width];
	        int row = 0, col = 0;
	        for(int pixel = 0; pixel < pixels.length; pixel += 3) {
	            int b = ((int) pixels[pixel]); // blue
	            int g = (((int) pixels[pixel + 1])); // green
	            int r = (((int) pixels[pixel + 2])); // red
	            result[row][col] = new PcpPixel(r, g, b);
	            System.out.println(result[row][col].toString());
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void compileCard() {
		
	}
	
	private class PcpPixel {
		public int r;
		public int g;
		public int b;
		public PcpPixel(int r, int g, int b) {
			this.r=r;
			this.g=g;
			this.b=b;
		}
		public String toString() {
			return r + ":" + g + ":" + b;
		}
	}
	
}
