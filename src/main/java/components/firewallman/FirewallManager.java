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

public class FirewallManager {
    // Defining global variables
    public volatile boolean firewallStatus; // Thread-safe flag
    private final Blocklist blockedIPs;
    private ArrayList<Integer> openPorts;
    private final ArrayList<Integer> allowedPorts;
    private final Map<InetAddress, Set<Integer>> ipPortsMap;
	private PortScanner ps;

    public FirewallManager(PortScanner ps) {
        this.firewallStatus = false;
        this.blockedIPs = new Blocklist();
        this.openPorts = new ArrayList<>();
        this.allowedPorts = new ArrayList<>();
        this.ipPortsMap = new ConcurrentHashMap<>();
		this.ps = ps;
    }

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

	public FirewallManager(ArrayList<Integer> openPorts) {
        this.firewallStatus = false;
        this.blockedIPs = new Blocklist();
        this.openPorts = new ArrayList<>(openPorts);
        this.allowedPorts = new ArrayList<>();
        this.ipPortsMap = new ConcurrentHashMap<>();
    }

	public FirewallManager(String[] blockedIPs, ArrayList<Integer> openPorts) {
        this.firewallStatus = false;
		Blockable[] bL = new Blockable[blockedIPs.length];
		for (int i = 0; i < blockedIPs.length; i++) {
			bL[i] = new Blockable("IP", blockedIPs[i]);
		}
        this.blockedIPs = new Blocklist(bL);
        this.openPorts = new ArrayList<>(openPorts);
        this.allowedPorts = new ArrayList<>();
        this.ipPortsMap = new ConcurrentHashMap<>();
    }

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
                                    if (!blockedIPs.checkBlocked(new Blockable("IP", srcIP))) {
                                        addBlockedIP(srcIP);
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

    public void stopFirewall() {
        this.firewallStatus = false;
    }

    public boolean checkBlockedIP(String ip) {
        Blockable blockedIP = new Blockable("IP", ip);
        return blockedIPs.checkBlocked(blockedIP);
    }

    public void addBlockedIP(String ip) {
        Blockable blockItem = new Blockable("IP", ip);
        if (!this.blockedIPs.checkBlocked(blockItem)) {
            this.blockedIPs.addListBlocked(blockItem);
        }
    }

    public void removeBlockedIP(String ip) {
        this.blockedIPs.removeListBlocked(new Blockable("IP", ip));
    }

    public boolean checkPortStatus(int port) {
        scanPorts();
        return this.openPorts.contains(port);
    }

    public void scanPorts() {
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