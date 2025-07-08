package client.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Arrays;
import shared.Link;
import shared.Encrypter;
import shared.Logger;

import javax.xml.crypto.Data;

public class TwoWayLink implements Link {

	private static final String hi = "Hi!";
	private Logger logger = null;
	private DatagramPacket send, receive = null;
	private DatagramSocket link = null;
	private InetAddress hostAddress = null;
	private Encrypter crypt = null;
	private byte[] headerBuffer = new byte[HEADER.getBytes().length];

	public TwoWayLink(String hostname) {

		try {
			logger = new Logger("logs/twowaylink.log", false);
			link = new DatagramSocket(PORT);
			hostAddress = InetAddress.getByName(hostname);
			crypt = new Encrypter();
			logger.write("TwoWayLink established.");
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
		}

	}

	public void sayHello() {

		try {
			send = new DatagramPacket(hi.getBytes(), hi.getBytes().length, hostAddress, PORT);
			link.send(send);
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
		}

	}

	public boolean checkHello() {

		try {
			receive = new DatagramPacket(hi.getBytes(), hi.getBytes().length);
			link.receive(receive);
			if (new String(receive.getData()).equals(hi))
				return true;
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
		}
		return false;

	}

	@Override
	public boolean sendMessage(String msg) {

		try {
			byte[] bbuffer = HEADER.getBytes();
			send = new DatagramPacket(bbuffer, bbuffer.length, hostAddress, PORT);
			link.send(send);
			bbuffer = msg.getBytes();
			send.setData(bbuffer);
			send.setLength(bbuffer.length);
			link.send(send);
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
		}
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
		} catch (IOException io) {
			io.printStackTrace();
		}
		return recvStr;

	}

	@Override
	public void close() {

		link.close();

	}

	public Encrypter getCrypt() { return crypt; }

	@Override
	public String toString() {

		return link.getLocalAddress().toString();

	}

}
