package components.firewallman;

import java.util.ArrayList;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
import components.blocklist.Blockable;
import components.blocklist.Blocklist;
import components.portscan.PortScanner;

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
		
	public void startFirewall() throws PcapNativeException {
		this.firewallStatus = true;
		while (firewallStatus) {
			try {
				PcapNetworkInterface nif = Pcaps.findAllDevs().get(0);
				int snapshotLength = 65536;
				int readTimeout = 50;

				PcapHandle handle = nif.openLive(snapshotLength, PcapNetworkInterface.PcapMode.PROMISCUOUS, readTimeout);

				PacketListener listener = packet -> {
					if (packet instanceof IpPacket) {
						IpPacket ipPacket = (IpPacket) packet;

						if (ipPacket.getPayload() instanceof TcpPacket) {
							TcpPacket tcpPacket = (TcpPacket) ipPacket.getPayload();
							if (tcpPacket.getTcpFlags().syn()) {
								InetAddress srcIp = ipPacket.getHeader().getSrcAddr();
								int destPort = tcpPacket.getHeader().getDstPort().valueAsInt();

								ipToPortsMap.putIfAbsent(srcIp, new HashSet<>());
								ipToPortsMap.get(srcIp).add(destPort);

								if (ipToPortsMap.get(srcIp).size() >= 5) {
									String srcIP = ipToPortsMap.get(srcIp);
									if (blockedIPs.checkBlocked(new blockable("IP", srcIP))) {
										addBlockedIP(srcIP);
									}
								}
							}
					}
				};
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {}
		}
	}
		
	public void stopFirewall() {
		this.firewallStatus = false;
	}
		
	public boolean checkBlockedIP(String ip) {
		Blockable blockedIP = new Blockable("IP", ip);
		return blockedIPs.checkBlocked(blockedIP);
	}
		
	public void addBlockedIP(String ip) {
		String id = "IP";
		String name = ip;
		
		Blockable blockItem = new Blockable(id, name);
		if (!this.blockedIPs.checkBlocked(blockItem)) {
			this.blockedIPs.addListBlocked(blockItem);
		}
	}
		
	public void removeBlockedIP(String ip) {
		String id = "IP";
		String name = ip;
		
		this.blockedIPs.removeListBlocked(new Blockable(id, name));
	}
		
	public boolean checkPortStatus(int port) {
		scanPorts();
		return this.openPorts.contains(port);
	}
		
	public void scanPorts() {
		PortScanner ps = new PortScanner(allowedPorts);
		this.openPorts = ps.getOpenPorts();

		ps.checkAuthorizations();
	}

    public void allowPort(int port) {
        allowedPorts.add(port);
    }

    public void disallowPort(int port) {
        allowedPorts.remove(port);
    }
}