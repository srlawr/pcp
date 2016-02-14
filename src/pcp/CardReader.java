package pcp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;

public class CardReader implements Runnable {

	private static final String JPG = "image/jpeg";
	private static final int SCALE = 2;
	public Path filepath;

    private PcpPixel[][] imageArray;
    private PcpPixel[][] finalArray;

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
			System.out.println("Non-Jpg file format: " + fileType);
			return;
		}
		
		processFile();
		averageBlocks();
		compileCard();
	}
	
	private void processFile() {
		try {
	        BufferedImage originalImage = ImageIO.read(filepath.toFile());

	        final byte[] pixels = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
	        final int width = originalImage.getWidth();
	        final int height = originalImage.getHeight();
	        
	        imageArray = new PcpPixel[height][width];
	        int row = 0, col = 0;
	        for(int pixel = 0; pixel < pixels.length; pixel += 3) {
	            int b = ((int) pixels[pixel] & 0xff); // blue
	            int g = (((int) pixels[pixel + 1]) & 0xff); // green
	            int r = (((int) pixels[pixel + 2]) & 0xff); // red
	            imageArray[row][col] = new PcpPixel(r, g, b);
	            //System.out.println(imageArray[row][col].toString());
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
	
	private void averageBlocks() {
		finalArray = new PcpPixel[imageArray.length/SCALE][imageArray[0].length/SCALE];
		for(int x = 0; x < imageArray.length; x += SCALE) {
			for(int y = 0; y < imageArray[0].length; y += SCALE) {
				int aveR = 0, aveG = 0, aveB = 0;
				for(int i = 0; i < SCALE; i++) {
					for(int j = 0; j < SCALE; j++) {
						aveR += imageArray[x + i][y + j].r;
						aveG += imageArray[x + i][y + j].g;
						aveB += imageArray[x + i][y + j].b;
					}
				}
				int scaleSq = SCALE * SCALE;
				finalArray[x/SCALE][y/SCALE] = new PcpPixel(aveR / scaleSq, aveG / scaleSq, aveB / scaleSq);
				//System.out.println(finalArray[x/SCALE][y/SCALE].toString());
			}
		}
	}
	
	private void compileCard() {
		System.out.println("compiling");
		for(int x = 0; x < finalArray.length; x++) {
			for(int y = 0; y < finalArray[0].length; y++) {
				System.out.print(finalArray[x][y].borw());
			}
			System.out.println();
		}
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
		public int borw() {
			if(((r + g + b) / 3) > 128) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	
}
