package components.settingshandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import components.blocklist.Blockable;
import components.blocklist.Blocklist;

/**
 * The SettingsHandler class is responsible for managing the settings file.
 * It provides functionality to save and retrieve blocked IPs and allowed ports.
 */
public class SettingsHandler {
    File settingsFile = new File("settings.txt");

    /**
     * Default constructor that initializes the settings file.
     * Ensures the file exists by calling {@link #checkFileExistence()}.
     */
    public SettingsHandler() {
        checkFileExistence();
    }

    /**
     * Constructor that accepts a custom file path for the settings file.
     * Ensures the file exists by calling {@link #checkFileExistence()}.
     * 
     * @param pathname The path to the settings file.
     */
    public SettingsHandler(String pathname) {
        this.settingsFile = new File(pathname);
        checkFileExistence();
    }

    /**
     * Checks if the settings file exists. If it does not exist, creates a new file.
     * Logs an error message if the file cannot be created.
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

    /**
     * Converts the blocked IPs in the given Blocklist to a formatted string.
     * 
     * @param bL The Blocklist containing blocked IPs.
     * @return A formatted string representing the blocked IPs.
     */
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

    /**
     * Converts the allowed ports in the given list to a formatted string.
     * 
     * @param aP The list of allowed ports.
     * @return A formatted string representing the allowed ports.
     */
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

    /**
     * Saves the allowed ports and blocked IPs to the settings file.
     * 
     * @param aP The list of allowed ports.
     * @param bL The Blocklist containing blocked IPs.
     */
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

    /**
     * Retrieves the blocked IPs from the settings file.
     * 
     * @return An array of blocked IPs as strings.
     */
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

    /**
     * Retrieves the allowed ports from the settings file.
     * 
     * @return A list of allowed ports as integers.
     */
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