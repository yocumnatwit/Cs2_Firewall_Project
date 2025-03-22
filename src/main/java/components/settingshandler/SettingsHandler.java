package components.settingshandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import components.blocklist.Blockable;
import components.blocklist.Blocklist;

public class SettingsHandler {
    File settingsFile = new File("settings.txt");
    

    public SettingsHandler() {
        checkFileExistence();
    }

    public SettingsHandler(String pathname) {
        this.settingsFile = new File(pathname);
        checkFileExistence();
    }

	/**
     * This method checks if the settingsFile exists.
     * If it does not exist, it creates a new file.
     * It is used to ensure that the warning log file is available for writing.
     */
    private void checkFileExistence() {
        if ( !settingsFile.exists() ) {
            try {
                boolean output = settingsFile.createNewFile();
                if ( !output ) {
                    System.err.println( "Error creating warning log file." );
                }
            } catch ( IOException e ) {
                System.err.println( "Error creating warning log file: " + e.getMessage() );
            }
        }
    }

    private String saveBlockedIPsStr(Blocklist bL) {
        StringBuilder content = new StringBuilder();
        content.append("Blocked IPs\n");
        ArrayList<Blockable> blocklist = bL.toArray();
        for (Blockable ip : blocklist) {
            content.append(ip.toString());
            content.append("\n");
        }
        content.append("|");
        content.append("\n");
        return content.toString();
    }

    private String saveAllowedPortsStr(ArrayList<Integer> aP) {
        StringBuilder content = new StringBuilder();
        content.append("Allowed Ports\n");
        for (Integer port : aP) {
            content.append(port.toString());
            content.append("\n");
        }
        content.append("|");
        content.append("\n");
        return content.toString();
    }

    public void savePortsIPs(ArrayList<Integer> aP, Blocklist bL) {
        StringBuilder sb = new StringBuilder();
        sb.append(saveAllowedPortsStr(aP));
        sb.append(saveBlockedIPsStr(bL));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
            writer.write(sb.toString());
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public String[] retrieveBlockedIPs() {
        ArrayList<String> blockedIps = new ArrayList<>();

        try (Scanner reader = new Scanner(settingsFile)) {
            boolean correctCompartment = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.equals("Blocked IPs")) {
                    correctCompartment = true;
                    continue;
                }
                if (correctCompartment) {
                    if (line.equals("|")) {
                        break; 
                    }
                    blockedIps.add(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve blocked IPs: " + e.getMessage());
        }

        return blockedIps.toArray(new String[0]);
    }

    public ArrayList<Integer> retrieveAllowedPorts() {
        ArrayList<Integer> allowedPorts = new ArrayList<>();

        try (Scanner reader = new Scanner(settingsFile)) {
            boolean correctCompartment = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.equals("Allowed Ports")) {
                    correctCompartment = true;
                    continue;
                }
                if (correctCompartment) {
                    if (line.equals("|")) {
                        break; 
                    }
                    try {
                        allowedPorts.add(Integer.parseInt(line)); 
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid port number: " + line);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Could not retrieve allowed ports: " + e.getMessage());
        }

        return allowedPorts; 
    }
}