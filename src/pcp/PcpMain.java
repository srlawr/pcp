package pcp;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class PcpMain {

	public static void main(String[] args) {
		try {
			CardboxWatcher cardboxWatcher = new CardboxWatcher("file:///srlawr/Java/watchfolder");
			cardboxWatcher.processEvents();
		} catch (PcpException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Apparantly that did not work.");
	}
	
}
