package components.threadhandler;

import java.util.ArrayList;

public class ThreadHandler extends Thread{

    private final int CORECOUNT = Runtime.getRuntime().availableProcessors();
    private final int MAXTHREADS = CORECOUNT * 2;
    
    private int currentThreads = 0;
    private ArrayList<Thread> runningThreads = new ArrayList<>();
    private ArrayList<Runnable> runningTasks = new ArrayList<>();


    public ThreadHandler(){}

    public synchronized void run(Runnable task) {
        if (currentThreads >= MAXTHREADS) {
            System.out.println("Max thread limit reached. Cannot start more threads.");
            return;
        }

        Thread taskThread = new Thread(() -> {
            try {
                task.run();  
                synchronized (this) {
                    runningTasks.remove(task);
                    runningThreads.remove(Thread.currentThread());
                    currentThreads--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        taskThread.start(); 
        synchronized (this) {
            runningTasks.add(task);
            runningThreads.add(taskThread);
            currentThreads++;
        }
    }

    
    public void sleepThread(Thread thread, int timeMillis) {
        if (runningThreads.contains(thread)) {
            try {
                Thread.sleep(timeMillis);
            } catch (InterruptedException e) {
                System.out.println("Thread sleep interrupted: " + thread.getName());
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
    }

   
    public void killThread(Thread thread) {
    	if (runningThreads.contains(thread)) {
            thread.interrupt();
            System.out.println("Killed thread: " + thread.getName());
        }
    }


    public void printThreads() {
        System.out.println("Currently running threads:");
        for (Thread thread : runningThreads) {
            System.out.println("Thread: " + thread.getName());
        }
    }


    public int getOpenThreads() {
        return MAXTHREADS - currentThreads;
    }
}