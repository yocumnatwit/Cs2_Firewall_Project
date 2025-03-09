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
		// TODO: Re-implement, This will take a long amount of time.
		Socket portCheck;
		String host = "localhost";
		ArrayList<Integer> openPorts = new ArrayList<>();
		
		for (int port = 1; port < 65535; port++) {
			try {
				portCheck = new Socket(host, port);
				openPorts.add(port);
			}
			catch (UnknownHostException e) { break; }
			catch (IOException e) {}
		}
		
		this.openPorts = openPorts;
	}
		
	private void sendWarning(String message) {
			
	}
		
}