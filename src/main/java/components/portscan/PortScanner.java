package components.portscan;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import components.threadhandler.ThreadHandler;
import components.warnman.WarningManager;

/**
 * The PortScanner class is responsible for scanning ports on a local machine,
 * managing authorized ports, and detecting unauthorized open ports.
 * It uses a ThreadHandler to manage concurrent port scanning tasks.
 */
public class PortScanner {
    private final ArrayList<Integer> authorizedPorts = new ArrayList<>();
    private ArrayList<Integer> openPorts = new ArrayList<>();
    public ThreadHandler th;

    /**
     * Constructs a PortScanner with a specified ThreadHandler.
     *
     * @param th the ThreadHandler to manage threads for port scanning
     */
    public PortScanner(ThreadHandler th) {
        this.th = th;
    }

    /**
     * Constructs a PortScanner with a list of authorized ports and a ThreadHandler.
     *
     * @param authPorts the list of authorized ports
     * @param th        the ThreadHandler to manage threads for port scanning
     */
    public PortScanner(ArrayList<Integer> authPorts, ThreadHandler th) {
        for (Integer port : authPorts) {
            authorizedPorts.add(port);
        }
        this.th = th;
    }

    /**
     * Sends a warning message to the WarningManager and logs it to a file.
     *
     * @param warning the warning message to be logged
     */
    private void sendWarning(String warning) {
        java.io.File warningLogFile = new java.io.File("warningLog.txt");
        WarningManager warnMan = new WarningManager(warningLogFile);

        warnMan.writeWarning(warning);
        warnMan.writeWarnings();
    }

    /**
     * Scans all ports on the local machine and returns a list of open ports.
     *
     * @return a list of open ports
     */
    public ArrayList<Integer> scanPorts() {
        Vector<Integer> detectedOpenPortsVec = new Vector<>();
        try {
            int openThreads = th.getOpenThreads();
            CountDownLatch latch = new CountDownLatch(openThreads);
            int start = 0;
            int end;
            for (int i = 0; i < openThreads; i++){
                end = 65535 / openThreads * (i + 1);
                int finalEnd = end;
                int finalStart = start;
                th.run(() -> {
                    try {
                        detectedOpenPortsVec.addAll(scanPorts(finalStart, finalEnd));
                    }finally {
                        latch.countDown();
                    }
                });
                start = end + 1;
            }
            latch.await();
        } catch (Exception e) {
            sendWarning(e.toString());
        }
        return new ArrayList<>(detectedOpenPortsVec);
    }

    /**
     * Scans a range of ports on the local machine and returns a list of open ports.
     *
     * @param start the starting port number
     * @param end   the ending port number
     * @return a list of open ports within the specified range
     */
    public ArrayList<Integer> scanPorts(int start, int end){
        ArrayList<Integer> openPorts = new ArrayList<>();
        for (int port = start; port <= end; port++) {
            try (java.net.Socket socket = new java.net.Socket()) {
                socket.connect(new java.net.InetSocketAddress("localhost", port), 200);
                openPorts.add(port);
            } catch (Exception ignored) {

            }
        }

        return openPorts;
    }

    /**
     * Returns the list of authorized ports.
     *
     * @return the list of authorized ports
     */
    public ArrayList<Integer> getAuthorizedPorts() {
        return authorizedPorts;
    }

    /**
     * Returns the list of currently open ports.
     * This method updates the list of open ports before returning it.
     *
     * @return the list of open ports
     */
    public ArrayList<Integer> getOpenPorts() {
        updateOpenPorts();
        return openPorts;
    }

    /**
     * Updates the list of open ports by scanning all ports.
     */
    public void updateOpenPorts() {
        openPorts = scanPorts();
    }

    /**
     * Adds a port to the list of authorized ports.
     *
     * @param port the port to be authorized
     */
    public void allowPort(int port) {
        authorizedPorts.add(port);
    }

    /**
     * Removes a port from the list of authorized ports.
     *
     * @param port the port to be disallowed
     */
    public void disallowPort(int port) {
        authorizedPorts.remove(port);
    }

    /**
     * Checks if a specific port is currently open.
     *
     * @param port the port to check
     * @return true if the port is open, false otherwise
     */
    public boolean checkportStatus(int port) {
        updateOpenPorts();
        return openPorts.contains(port);
    }

    /**
     * Checks for unauthorized open ports and sends warnings for any detected.
     */
    public void checkAuthorizations() {
        updateOpenPorts();
        for (Integer port : openPorts) {
            if (!authorizedPorts.contains(port)) {
                sendWarning(String.format("Warning detected unauthorized port: %d", port));
            }
        }
    }
}