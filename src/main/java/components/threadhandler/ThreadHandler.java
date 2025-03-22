package components.threadhandler;

import java.io.IOException;
import java.util.ArrayList;

// TODO: Still must be completed.
public class ThreadHandler extends Thread{

    private final int CORECOUNT = Runtime.getRuntime().availableProcessors();
    private final int MAXTHREADS = CORECOUNT * 2;
    
    private int currentThreads = 0;
    private ArrayList<Thread> runningThreads = new ArrayList<>();
    private ArrayList<Process> runningProcesses = new ArrayList<>();

    //+ run(process: String): void
    public synchronized void run(String process) {
        if (currentThreads >= MAXTHREADS) {
            System.out.println("Max thread limit reached. Cannot start more processes.");
            return;
        }

        Thread processThread = new Thread(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(process);
                Process proc = builder.start();
                synchronized (this) {
                    runningProcesses.add(proc);
                    runningThreads.add(Thread.currentThread());
                    currentThreads++;
                }

               
                while (proc.isAlive()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                synchronized (this) {
                    runningProcesses.remove(proc);
                    runningThreads.remove(Thread.currentThread());
                    currentThreads--;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        processThread.start();
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
        System.out.println("Currently running processes:");
        for (Process proc : runningProcesses) {
            System.out.println(proc.info().commandLine());
        }
    }
    
}