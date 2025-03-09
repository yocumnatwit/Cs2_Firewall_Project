package components.firewallman;

import java.util.ArrayList;
import java.net.*;
import java.io.*;

import components.blocklist.Blocklist;
import components.blocklist.Blockable;

public class FirewallManager {
	// Defining global variables
	public boolean firewallStatus;
	private Blocklist blockedIPs;
	private ArrayList<Integer> openPorts;
	
	// TODO: Take a look at how this should be used. Currently no function can use it.
	private ArrayList<Integer> allowedPorts;
		
	public FirewallManager() {
		// Defaults
		this.firewallStatus = false;
		this.blockedIPs = new Blocklist();
		this.openPorts = new ArrayList<>();
		this.allowedPorts = new ArrayList<>();
	}
		
	public FirewallManager(String[] blockedIPs) {
		// Defaults
		this.firewallStatus = false;
		this.openPorts = new ArrayList<>();
		this.allowedPorts = new ArrayList<>();
		
		// Populate blockedIPs
		for (int i = 0; i < blockedIPs.length; i++) {
			addBlockedIP(blockedIPs[i]);
		}
	}
		
	// openPorts: List<int>
	public FirewallManager(ArrayList<Integer> openPorts) {
		// Defaults
		this.firewallStatus = false;
		this.blockedIPs = new Blocklist();
		this.allowedPorts = new ArrayList<>();
		
		// Populate openPorts
		this.openPorts = openPorts;
	}
		
	// + Firewall(openPorts: List<Integer>, blockedIPs: List<String>) 
	public FirewallManager(ArrayList<Integer> openPorts, String[] blockedIPs) {
		// Defaults
		this.firewallStatus = false;
		this.allowedPorts = new ArrayList<>();
		
		// Populate openPorts
		this.openPorts = openPorts;
				
		// Populate blockedIPs
		for (int i = 0; i < blockedIPs.length; i++) {
			addBlockedIP(blockedIPs[i]);
		}
	}
		
	public void startFirewall() {
		this.firewallStatus = true;
	}
		
	public void stopFirewall() {
		this.firewallStatus = false;
	}
		
	public boolean checkBlockedIP(String ip) {
		// TODO: Should we check blocked port instead?
		// TODO: Need to have a way to define id and name.
		String id = ip;
		String name = "";
		
		Blockable block = new Blockable(id, name);
		return this.blockedIPs.checkBlocked(block);
	}
		
	public void addBlockedIP(String ip) {
		String id = ip;
		String name = ""; // TODO: Unsure what the name field should be for object Blockable.
		
		Blockable blockItem = new Blockable(id, name);
		if (!this.blockedIPs.checkBlocked(blockItem)) {
			this.blockedIPs.addListBlocked(blockItem);
		}
	}
		
	public void removeBlockedIP(String ip) {
		// TODO: Need to have a way to define id and name.
		String id = ip;
		String name = "";
		
		this.blockedIPs.removeListBlocked(new Blockable(id, name));
	}
		
	public boolean checkPortStatus(int port) {
		// Returns true if port is open.
		return this.openPorts.contains(port);
	}
		
	public void scanPorts() {
		int lineCount = 0;
		
		// https://askubuntu.com/questions/538443/whats-the-difference-between-port-status-listening-time-wait-close-wait
		try {
			Process process = new ProcessBuilder("netstat", "-antd").start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			
			
			// Process each line
			while(((line = reader.readLine()) != null)) {
				// Skip first two lines
				if (lineCount <= 1) {
					continue;
				}
				
				// Split by whitespace
				String[] results = line.split("\\s+");
				
				// Line may be processed wrong so we should skip
				if (results.length != 6) {
					continue;
				}
				
				String state = results[5];
				if (state.equals("CLOSE")) {
					continue;
				}
				
				String localAddress = results[3];
				String portString = localAddress.split(":")[1];
				
				try {
					int port = Integer.parseInt(portString);
					this.openPorts.add(port);
				} catch (NumberFormatException e) {
					continue;
				}
				
				
			}
			
			reader.close();
			process.waitFor();
			
		} catch(IOException | InterruptedException e) {
			// TODO: Possibly implement some way to show that scanPorts failed.
			// TODO: Add error in-case netstat is not installed.
			return;
		}
	}
		
	private void sendWarning(String message) {
	}
		
}