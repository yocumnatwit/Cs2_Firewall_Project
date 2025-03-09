package components.threadhandler;

import java.io.IOException;
import java.util.ArrayList;

// TODO: Still must be completed.
public class ThreadHandler extends Thread{

    private final int CORECOUNT = Runtime.getRuntime().availableProcessors();
    private final int MAXTHREADS = CORECOUNT * 2;
    
    private int currentThreads = 0;
    private ArrayList<Process> runningProcesses = new ArrayList<>();

    //+ run(process: String): void
    public void run(String process) {
    	if (currentThreads == MAXTHREADS) {
    		return;
    	}
    	
        ProcessBuilder builder = new ProcessBuilder(process);
        try {
        	Process proc = builder.start();
        	runningProcesses.add(proc);
        	currentThreads++;
        	
        } catch(IOException e) {}
    }
    //+ run(process: String, port: int): void
    public void run(String process, int Port) {
    	
    }
    //+ pauseThread(process: String): void
    public void pauseThread(String process) {
    	
    }
    //+ killThread(process: String)
    public void killThread(String process) {
    	
    }
    //+ printThread(): void
    public void printThread() {
    	
    }
    
}