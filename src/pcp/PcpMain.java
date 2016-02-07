package pcp;

public class PcpMain {
	
	private static final String URI = "file:///srlawr/Java/watchfolder";

	public static void main(String[] args) {
		try {
			CardboxWatcher cardboxWatcher = new CardboxWatcher(URI);
			cardboxWatcher.processEvents();
		} catch (PcpException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Apparantly that did not work.");
	}
	
}
