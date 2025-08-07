package client.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.AsynchronousCloseException;
import client.util.Encrypter;
import client.util.Logger;
import client.util.UserNetworkIdentifier;

public class TwoWayLink implements Link {

	private static final String hi = "Hi!";
	private Logger logger = null;
	private DatagramPacket send, receive = null;
	private DatagramSocket link = null;
	private InetAddress hostAddress = null;
	private int hostPort = 0;
	private Encrypter crypt = null;
	private byte[] headerBuffer = new byte[HEADER.getBytes().length];

	public TwoWayLink(UserNetworkIdentifier id, DatagramSocket link) {

		try {
			logger = new Logger("logs/twowaylink.log", false);
			String s1 = UserNetworkIdentifier.decrypt(id);
			hostAddress = InetAddress.getByName(s1.substring(0, s1.indexOf(':')));
			hostPort = Integer.parseInt(s1.substring(s1.indexOf(':') + 1));
			this.link = link;
			StringBuilder sb = new StringBuilder("TwoWayLink established with ");
			sb.append(hostAddress.toString());
			sb.append(':');
			sb.append(hostPort);
			sb.append('.');
			logger.write(sb.toString());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
		}

	}

	public InetAddress getHostAddress() {

		return hostAddress;

	}

	public int getHostPort() {

		return hostPort;

	}

	public void sayHello() {

		try {
			send = new DatagramPacket(hi.getBytes(), hi.getBytes().length, hostAddress, hostPort);
			link.send(send);
		} catch (IOException io) {
			throw new RuntimeException(io);
		}

	}

	public boolean checkHello() {

		try {
			receive = new DatagramPacket(hi.getBytes(), hi.getBytes().length);
			link.receive(receive);
			if (new String(receive.getData()).equals(hi)) {
				logger.write("Accepted associate hello.");
				return true;
			}
			logger.write("Rejected unknown message.");
		} catch (IOException io) {
			throw new RuntimeException(io);
		}
		return false;

	}

	@Override
	public boolean sendMessage(String msg) {

		try {
			byte[] bbuffer = HEADER.getBytes();
			send = new DatagramPacket(bbuffer, bbuffer.length, hostAddress, hostPort);
			link.send(send);
			bbuffer = msg.getBytes();
			send.setData(bbuffer);
			send.setLength(bbuffer.length);
			link.send(send);
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
		}
		logger.write("Sent Swiss message to associate.");
		return true;

	}

	@Override
	public String recvMessage() {

		String recvStr = null;
		byte[] bbuffer = null;

		try {
			receive = new DatagramPacket(headerBuffer, headerBuffer.length);
			link.receive(receive);
			if (new String(receive.getData()).equals(HEADER)) {
				bbuffer = new byte[256];
				receive = new DatagramPacket(bbuffer, bbuffer.length);
				link.receive(receive);
				recvStr = new String(receive.getData());
			}
		} catch (AsynchronousCloseException | SocketException e) {} catch (IOException e) {
            throw new RuntimeException(e);
        }
		logger.write("Received Swiss message from associate.");
        return recvStr;

	}

	@Override
	public void close() {

		link.close();
		logger.write("Closed TwoWayLink.");

	}

	public Encrypter getCrypt() { return crypt; }

	@Override
	public String toString() {

		return link.getLocalAddress().toString();

	}

}
