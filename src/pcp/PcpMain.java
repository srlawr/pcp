package pcp;

import java.io.File;
import java.nio.file.Path;

public class PcpMain {
	
	private static final String URI = "file:///srlawr/Java/watchfolder";
	private static final String TESTCARD = "/src/pcp/testCard.jpg";
	private static final String INSTRUCTIONS = "/src/pcp/Instructions_v1.csv";

	private static final String TEST = "test";
	
	public static void main(String[] args) {
		System.out.println("running in " + args[0] + " mode");
		System.out.println(Byte.parseByte("1110",2));

		File instructionFile = new File(System.getProperty("user.dir"), INSTRUCTIONS);
		System.out.println(instructionFile.exists());

		
		if(TEST.equalsIgnoreCase(args[0])) {

			// Grab test file
			File myFile = new File(System.getProperty("user.dir"), TESTCARD);
			System.out.println(myFile.exists());

			// Get path from file to pass to cardreader
			Path testPath = myFile.toPath();
			System.out.println(testPath);

			// Send the card processor off on another thread
			Thread processCardThread = new Thread(new CardReader(testPath, new InstructionSet(instructionFile)));
			processCardThread.start();
			
		} else {

			try {
				CardboxWatcher cardboxWatcher = new CardboxWatcher(URI, new InstructionSet(instructionFile));
				cardboxWatcher.processEvents();
			} catch (PcpException e) {
				System.out.println(e.getMessage());
			}

			System.out.println("Apparantly that did not work.");
		}
	}
	
}
