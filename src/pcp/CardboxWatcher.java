package pcp;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
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
public class CardboxWatcher {

	private InstructionSet instructionSet;

	private WatchService watcher;
	private Path pathKey;
	
    public CardboxWatcher(String dirPath, InstructionSet instructions) throws PcpException {
		this.instructionSet = instructions;
    	setupWatchService();
    	registerFolder(dirPath);
    }

    private void setupWatchService() throws PcpException {
    	try {
    		this.watcher = FileSystems.getDefault().newWatchService();
    	} catch (IOException e) {
    		throw new PcpException("Could not establish the NIO service");
    	}
    }
    
    private void registerFolder(String directoryPath) throws PcpException { 
		try {
			pathKey = Paths.get(new URI(directoryPath));
			WatchKey key = pathKey.register(watcher, ENTRY_CREATE);
		} catch (URISyntaxException e) {
			throw new PcpException("Could not find resource: " + e.getMessage());
		} catch (IOException e) {
			throw new PcpException("Could not register directory with watcher: " + e.getMessage());
		}
		System.out.format("Registered folder %s\n", directoryPath);
	}

	public void processEvents() throws PcpException {
		String lastChild = ""; // each event potentially causes 2 cycles, so don't re-process an event if it is the same child as last time
        for (;;) {

        	WatchKey key;
            try {
            	// this is where we wait for the event
                key = watcher.take();
            } catch (InterruptedException e) {
                throw new PcpException("Folder watcher was interupped: " + e.getMessage());
            }
            
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
 
                if (OVERFLOW == kind) {
                	key.cancel();
                	throw new PcpException("There was a Cardbox overflow. Please tidy up your cards.");
                }
 
                if(ENTRY_CREATE == kind) {
	                WatchEvent<Path> ev = (WatchEvent<Path>) event;
	                Path child = pathKey.resolve(ev.context());

                	if(!child.getFileName().toString().equals(lastChild)) {
    	                System.out.format("%s: %s - processing.\n", event.kind().name(), child);
    	                
    	                Thread processCardThread = new Thread(new CardReader(child, instructionSet));
    	                processCardThread.start();
    	                
    	                lastChild = child.getFileName().toString();
                	} else {
                		System.out.println("De-duped");
                	}
                }

            }
 
            boolean valid = key.reset();
            if (!valid) {
            	key.cancel();
                throw new PcpException("Directory not accessible");
            }
        }
    }   
    
}
