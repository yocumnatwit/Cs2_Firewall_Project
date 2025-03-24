package components.firewallman;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

import components.blocklist.Blockable;
import components.blocklist.Blocklist;
import components.portscan.PortScanner;
import components.warnman.WarningManager;

/**
 * The FirewallManager class manages the firewall operations, including blocking IPs,
 * monitoring open ports, and handling port scanning. It uses a PortScanner to update
 * open ports and a Blocklist to manage blocked IPs.
 */
public class FirewallManager {
    // Defining global variables
    public volatile boolean firewallStatus; // Thread-safe flag
    private final Blocklist blockedIPs;
    private ArrayList<Integer> openPorts;
    private final ArrayList<Integer> allowedPorts;
    private final Map<InetAddress, Set<Integer>> ipPortsMap;
	private PortScanner ps;

    /**
     * Constructor to initialize the FirewallManager with a PortScanner.
     *
     * @param ps the PortScanner instance used for port scanning.
     */
    public FirewallManager(PortScanner ps) {
        this.firewallStatus = false;
        this.blockedIPs = new Blocklist();
        this.openPorts = new ArrayList<>();
        this.allowedPorts = new ArrayList<>();
        this.ipPortsMap = new ConcurrentHashMap<>();
		this.ps = ps;
    }

    /**
     * Constructor to initialize the FirewallManager with a list of blocked IPs and a PortScanner.
     *
     * @param blockedIPs an array of IP addresses to be blocked.
     * @param ps the PortScanner instance used for port scanning.
     */
	public FirewallManager(String[] blockedIPs, PortScanner ps) {
        this.firewallStatus = false;
		Blockable[] bL = new Blockable[blockedIPs.length];
		for (int i = 0; i < blockedIPs.length; i++) {
			bL[i] = new Blockable("IP", blockedIPs[i]);
		}
        this.blockedIPs = new Blocklist(bL);
        this.openPorts = new ArrayList<>();
        this.allowedPorts = new ArrayList<>();
        this.ipPortsMap = new ConcurrentHashMap<>();
		this.ps = ps;
    }

    /**
     * Constructor to initialize the FirewallManager with allowed ports and a PortScanner.
     *
     * @param allowedPorts a list of ports that are allowed.
     * @param ps the PortScanner instance used for port scanning.
     */
	public FirewallManager(ArrayList<Integer> allowedPorts, PortScanner ps) {
        this.firewallStatus = false;
        this.blockedIPs = new Blocklist();
        this.openPorts = new ArrayList<>();
        this.allowedPorts = new ArrayList<>(allowedPorts);
        this.ipPortsMap = new ConcurrentHashMap<>();
        this.ps = ps;
    }

    /**
     * Constructor to initialize the FirewallManager with blocked IPs, allowed ports, and a PortScanner.
     *
     * @param blockedIPs an array of IP addresses to be blocked.
     * @param allowedPorts a list of ports that are allowed.
     * @param ps the PortScanner instance used for port scanning.
     */
	public FirewallManager(String[] blockedIPs, ArrayList<Integer> allowedPorts, PortScanner ps) {
        this.firewallStatus = false;
		Blockable[] bL = new Blockable[blockedIPs.length];
		for (int i = 0; i < blockedIPs.length; i++) {
			bL[i] = new Blockable("IP", blockedIPs[i]);
		}
        this.blockedIPs = new Blocklist(bL);
        this.openPorts = new ArrayList<>();
        this.allowedPorts = new ArrayList<>(allowedPorts);
        this.ipPortsMap = new ConcurrentHashMap<>();
        this.ps = ps;
    }

    /**
     * Starts the firewall, enabling packet monitoring and blocking based on rules.
     *
     * @throws PcapNativeException if there is an error accessing the network interface.
     */
    public void startFirewall() throws PcapNativeException {
        this.firewallStatus = true;
        try {
            PcapNetworkInterface nif = Pcaps.findAllDevs().get(0);
            int snapshotLength = 65536;
            int readTimeout = 50;

            try (PcapHandle handle = nif.openLive(snapshotLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, readTimeout)) {
                PacketListener listener = packet -> {
                    IpPacket ipPacket = packet.get(IpPacket.class);
                    if (ipPacket != null) {

                        if (ipPacket.getPayload() instanceof TcpPacket tcpPacket) {
                            if (tcpPacket.getHeader().getSyn()) {
                                InetAddress srcIp = ipPacket.getHeader().getSrcAddr();
                                int destPort = tcpPacket.getHeader().getDstPort().valueAsInt();

                                ipPortsMap.putIfAbsent(srcIp, new HashSet<>());
                                ipPortsMap.get(srcIp).add(destPort);

                                if (ipPortsMap.get(srcIp).size() >= 5) {
                                    String srcIP = srcIp.getHostAddress();
                                    if (checkBlockedIP(srcIP)) {
                                        WarningManager wM = new WarningManager();
                                        wM.writeWarning(String.format("Prievously Blocked IP Traffic on IP: %s", srcIP));
                                    }
                                    if (!blockedIPs.checkBlocked(new Blockable("IP", srcIP))) {
                                        addBlockedIP(srcIP);
                                        WarningManager wM = new WarningManager();
                                        wM.writeWarning(String.format("Suspicious IP: %s", srcIP));
                                    }
                                }
                            }
                        }
                    }
                };

                while (firewallStatus) {
                    handle.loop(1, listener);
					ps.updateOpenPorts();
                }
            }
        } catch (Exception e) {
			System.out.println(e.toString());
        }
    }

    /**
     * Stops the firewall, disabling packet monitoring.
     */
    public void stopFirewall() {
        this.firewallStatus = false;
    }

    /**
     * Checks if a given IP address is blocked.
     *
     * @param ip the IP address to check.
     * @return true if the IP is blocked, false otherwise.
     */
    public boolean checkBlockedIP(String ip) {
        Blockable blockedIP = new Blockable("IP", ip);
        return blockedIPs.checkBlocked(blockedIP);
    }

    /**
     * Adds an IP address to the blocklist.
     *
     * @param ip the IP address to block.
     */
    public void addBlockedIP(String ip) {
        Blockable blockItem = new Blockable("IP", ip);
        if (!this.blockedIPs.checkBlocked(blockItem)) {
            this.blockedIPs.addListBlocked(blockItem);
        }
    }

    /**
     * Removes an IP address from the blocklist.
     *
     * @param ip the IP address to unblock.
     */
    public void removeBlockedIP(String ip) {
        this.blockedIPs.removeListBlocked(new Blockable("IP", ip));
    }

    /**
     * Checks if a specific port is open.
     *
     * @param port the port number to check.
     * @return true if the port is open, false otherwise.
     */
    public boolean checkPortStatus(int port) {
        scanPorts();
        return this.openPorts.contains(port);
    }

    /**
     * Scans for open ports and updates the list of open ports.
     */
    public void scanPorts() {
        this.openPorts = ps.getOpenPorts();
        ps.checkAuthorizations();
    }

    /**
     * Allows a specific port by adding it to the allowed ports list.
     *
     * @param port the port number to allow.
     */
    public void allowPort(int port) {
        allowedPorts.add(port);
    }

    /**
     * Disallows a specific port by removing it from the allowed ports list.
     *
     * @param port the port number to disallow.
     */
    public void disallowPort(int port) {
        allowedPorts.remove(port);
    }

    /**
     * Retrieves the blocklist of IPs.
     *
     * @return the Blocklist instance containing blocked IPs.
     */
    public Blocklist getBlockedIPs(){
        return blockedIPs;
    }

    /**
     * Retrieves the list of allowed ports.
     *
     * @return an ArrayList of allowed port numbers.
     */
    public ArrayList<Integer> getAllowedPorts() {
        return allowedPorts;
    }
}