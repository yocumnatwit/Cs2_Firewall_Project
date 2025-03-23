package components.threadhandler;

import java.util.ArrayList;

/**
 * The {@code ThreadHandler} class manages the execution of threads and tasks.
 * It ensures that the number of concurrently running threads does not exceed
 * the maximum allowed based on the available processor cores.
 */
public class ThreadHandler extends Thread {

    /**
     * The number of available processor cores.
     */
    private final int CORECOUNT;

    /**
     * The maximum number of threads that can run concurrently.
     */
    private final int MAXTHREADS;

    /**
     * The current number of running threads.
     */
    private int currentThreads = 0;

    /**
     * A list of currently running threads.
     */
    private final ArrayList<Thread> runningThreads = new ArrayList<>();

    /**
     * A list of currently running tasks.
     */
    private final ArrayList<Runnable> runningTasks = new ArrayList<>();

    /**
     * Constructs a {@code ThreadHandler} instance and initializes the core count
     * and maximum thread limit.
     */
    public ThreadHandler() {
        CORECOUNT = Runtime.getRuntime().availableProcessors();
        MAXTHREADS = CORECOUNT * 2;
    }

    /**
     * Executes a given task in a new thread if the maximum thread limit has not been reached.
     *
     * @param task the task to be executed
     */
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

    /**
     * Puts a specified thread to sleep for a given duration.
     *
     * @param thread     the thread to be put to sleep
     * @param timeMillis the duration in milliseconds for which the thread should sleep
     */
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

    /**
     * Interrupts and terminates a specified thread.
     *
     * @param thread the thread to be terminated
     */
    public void killThread(Thread thread) {
        if (runningThreads.contains(thread)) {
            thread.interrupt();
            System.out.println("Killed thread: " + thread.getName());
        }
    }

    /**
     * Prints the names of all currently running threads.
     */
    public void printThreads() {
        System.out.println("Currently running threads:");
        for (Thread thread : runningThreads) {
            System.out.println("Thread: " + thread.getName());
        }
    }

    /**
     * Returns the number of threads that can still be started without exceeding the limit.
     *
     * @return the number of available thread slots
     */
    public int getOpenThreads() {
        return MAXTHREADS - currentThreads;
    }
}