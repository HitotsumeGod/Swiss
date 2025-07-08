package client.net;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import shared.Logger;
import shared.Link;

public class OneWayLink implements Link {

	private Logger logger = null;
	private DatagramSocket link = null;
	private DatagramPacket packet = null;
	private InetAddress hostAddress = null;

	public OneWayLink(String hostname) {

		try {
			logger = new Logger("logs/onewaylink.log", false);
			link = new DatagramSocket(PORT);
			hostAddress = InetAddress.getByName(hostname);
		} catch (IOException io) {
			logger.write("IOException when creating socket.");
		}

	}

	@Override
	public boolean sendMessage(String msg) {

		byte[] bbuffer = msg.getBytes();

		try {
			packet = new DatagramPacket(bbuffer, bbuffer.length, hostAddress, PORT);
			link.send(packet);
		} catch (IOException io) {
			logger.write("IOException when writing to linked host.");
			return false;
		}
		return true;

	}

	@Override
	public String recvMessage() { return null; }

	@Override
	public void close() {

		link.close();

	}



}