package components.firewallman;

import java.util.ArrayList;

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
			// Id is just the index
			String id = Integer.toString(i);
			Blockable blockItem = new Blockable(id, blockedIPs[i]);
			
			// Add if not present in list
			if (!this.blockedIPs.checkBlocked(blockItem)) {
				this.blockedIPs.addListBlocked(blockItem);
			}
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
		
	}
		
	public void startFirewall() {
			
	}
		
	public void stopFirewall() {
			
	}
		
	public boolean checkBlockedIP(String ip) {
			
	}
		
	public void addBlockedIP(String ip) {
			
	}
		
	public void removeBlockedIP(String ip) {
			
	}
		
	public boolean checkPortStatus(int port) {
			
	}
		
	public void scanPorts() {
			
	}
		
	private void sendWarning(String message) {
			
	}
		
}