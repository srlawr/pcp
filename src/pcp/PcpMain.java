package pcp;

import java.io.File;
import java.nio.file.Path;

public class PcpMain {
	
	private static final String URI = "file:///srlawr/Java/watchfolder";
	private static final String TESTCARD = "/src/pcp/testCard.jpg";

	private static final String TEST = "test";
	
	public static void main(String[] args) {
		System.out.println("running in " + args[0] + " mode");
		if(TEST.equalsIgnoreCase(args[0])) {
			File myFile = new File(System.getProperty("user.dir"), TESTCARD);
			System.out.println(myFile.exists());
			Path testPath = myFile.toPath();
			System.out.println(testPath);
			Thread processCardThread = new Thread(new CardReader(testPath));
            processCardThread.start();
		} else {
			try {
				CardboxWatcher cardboxWatcher = new CardboxWatcher(URI);
				cardboxWatcher.processEvents();
			} catch (PcpException e) {
				System.out.println(e.getMessage());
			}
			System.out.println("Apparantly that did not work.");
		}
	}
	
}
