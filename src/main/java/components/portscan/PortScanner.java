package components.portscan;

import java.util.ArrayList;
import java.util.Vector;

import components.threadhandler.ThreadHandler;
import components.warnman.WarningManager;

public class PortScanner {
    private ArrayList<Integer> authorizedPorts = new ArrayList<>();
    private ArrayList<Integer> openPorts = new ArrayList<>();

    public PortScanner() {}

    public PortScanner(ArrayList<Integer> authPorts) {
        for (Integer port : authPorts) {
            this.authorizedPorts.add(port);
        }
    }

    private void sendWarning(String warning) {
        java.io.File warningLogFile = new java.io.File("warningLog.txt");
        WarningManager warnMan = new WarningManager(warningLogFile);

        warnMan.writeWarning(warning);
        warnMan.writeWarnings();
    }

    public ArrayList<Integer> scanPorts(ThreadHandler th) {
        Vector<Integer> detectedOpenPortsVec = new Vector<>();
        try {
            int openThreads = th.getOpenThreads();
            for (int i = 0; i < openThreads; i++){
                th.run(detectedOpenPortsVec.addAll(scanPorts()))
            }

        } catch (Exception e) {
            sendWarning(e.toString());
        }
        return new ArrayList<>(detectedOpenPortsVec);
    }

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




    public ArrayList<Integer> getAuthorizedPorts() {
        return authorizedPorts;
    }

    public ArrayList<Integer> getOpenPorts() {
        updateOpenPorts();
        return openPorts;
    }

    public void updateOpenPorts() {
        openPorts = scanPorts();
    }

    public void allowPort(int port) {
        authorizedPorts.add(port);
    }

    public void disallowPort(int port) {
        authorizedPorts.remove(port);
    }

    public boolean checkportStatus(int port) {
        updateOpenPorts();
        return openPorts.contains(port);
    }

    public void checkAuthorizations() {
        updateOpenPorts();
        for (Integer port : openPorts) {
            if (!authorizedPorts.contains(port)) {
                sendWarning(String.format("Warning detected unauthorized port: %d", port));
            }
        }
    }
}