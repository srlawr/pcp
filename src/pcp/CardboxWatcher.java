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

public class CardboxWatcher {

	private WatchService watcher;
    private Map<WatchKey,Path> keys;
    private final String directoryPath;

    public CardboxWatcher(String dirPath) throws PcpException {
    	this.directoryPath = dirPath;
    	setupWatchService();
    	registerFolder();
    }

    private void setupWatchService() throws PcpException {
    	this.keys = new HashMap<WatchKey,Path>();
    	try {
    		this.watcher = FileSystems.getDefault().newWatchService();
    	} catch (IOException e) {
    		throw new PcpException("Could not establish the NIO service");
    	}
    }
    
    private void registerFolder() throws PcpException { 
		try {
			Path dir = Paths.get(new URI(directoryPath));
			register(dir);
		} catch (URISyntaxException e) {
			throw new PcpException("Could not find resource: " + e.getMessage());
		} catch (IOException e) {
			throw new PcpException("Could not register directory: " + e.getMessage());
		}
	}
    
	private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        Path prev = keys.get(key);
        if (prev == null) {
            System.out.format("register: %s\n", dir);
        } else {
            if (!dir.equals(prev)) {
                System.out.format("update: %s -> %s\n", prev, dir);
            }
        }
        keys.put(key, dir);
    }
 
	public void processEvents() {
        for (;;) {
        	
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
 
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
 
                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }
 
                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path name = ev.context();
                Path child = dir.resolve(name);
 
                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);
            }
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }   
    
}
