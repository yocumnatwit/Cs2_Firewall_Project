package components.portscan;

import java.util.ArrayList;

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

    public ArrayList<Integer> scanPorts() {
        ArrayList<Integer> detectedOpenPorts = new ArrayList<>();
        try {
            for (int port = 1; port <= 65535; port++) {
                try (java.net.Socket socket = new java.net.Socket()) {
                   socket.connect(new java.net.InetSocketAddress("localhost", port), 200);
                    detectedOpenPorts.add(port);
                } catch (Exception ignored) {

                }
            }
        } catch (Exception e) {
            sendWarning(e.toString());
        }
        return detectedOpenPorts;
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