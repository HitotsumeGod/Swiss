package shared.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.channels.AsynchronousCloseException;
import java.security.SecureRandom;
import java.util.Arrays;
import shared.Logger;
import shared.UserNetworkIdentifier;
import shared.PubKeyLock;
import shared.SymKeyLock;
import shared.Hasher;

/**
 * The Link class is Swiss's networking abstraction.
 * A Link offers several services:
 * <p>
 * > Sending and receiving messages of arbitrary content via the
 * User Datagram Protocol (RFC 768).
 * <p></p>
 * > Retrieving the host's translated ip:port pair from a remote STUN server.
 * <p></p>
 */
public class Link {

	private static final String VERS = "SWISS V1.3E";
	public static final int BINDPORT = 13692;
	private static final int STUNHDRLEN = 20;
	private static final int STUNIDLEN = 12;
	private static final String HEADER = VERS + " xX%67%Xx";
	private static final int HEADERLEN = HEADER.length();
	private static final int MAXMSGLEN = 256;
	private static final int[] MAGICCOOKIE = { 0x21, 0x12, 0xA4, 0x42 };
	private static final InetSocketAddress[] STUNSERVERS = {
			new InetSocketAddress("stun.l.google.com", 19302),
			new InetSocketAddress("stun1.l.google.com", 3478),
			new InetSocketAddress("stun2.l.google.com", 19302),
			new InetSocketAddress("stun3.l.google.com", 3478),
			new InetSocketAddress("stun4.l.google.com", 19302)
	};
	private static final byte[] hello = { (byte) 0x06, (byte) 0x07 };
	private final Logger lincoln;
	private final PubKeyLock pkey;
	private final DatagramSocket dsock;
	private InetSocketAddress peerAddress;
	private DatagramPacket send, receive;

	public Link() {

		lincoln = new Logger("logs/twowaylink.log", false);
		pkey = new PubKeyLock();
        try {
            dsock = new DatagramSocket(BINDPORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

	public Link(UserNetworkIdentifier id) {

		try {
			lincoln = new Logger("logs/twowaylink.log", false);
			pkey = new PubKeyLock();
			dsock = new DatagramSocket(BINDPORT);
			setPeer(id);
			lincoln.write("Link established with " + peerAddress.getAddress().toString() +
					':' +
					peerAddress.getPort() +
					'.');
		} catch (IOException io) {
			throw new RuntimeException(io);
		}

	}
	
	public void setPeer(UserNetworkIdentifier id) {

		String decrypted = UserNetworkIdentifier.decrypt(id);
        try {
            peerAddress = new InetSocketAddress(InetAddress.getByName(decrypted.substring(0, decrypted.indexOf(':'))),
                    Integer.parseInt(decrypted.substring(decrypted.indexOf(':') + 1)));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

	public void sayHello() {

		try {
			send = new DatagramPacket(hello, hello.length, peerAddress.getAddress(), peerAddress.getPort());
			dsock.send(send);
		} catch (IOException io) {
			throw new RuntimeException(io);
		}

	}

	public boolean checkHello() {

		try {
			receive = new DatagramPacket(hello, hello.length);
			dsock.receive(receive);
			if (new String(receive.getData()).equals(hello)) {
				lincoln.write("Accepted associate hello.");
				return true;
			}
			lincoln.write("Rejected unknown message.");
		} catch (IOException io) {
			throw new RuntimeException(io);
		}
		return false;

	}

	public void sendMessage(String msg) {

		try {
			byte[] bbuffer = new byte[HEADERLEN + msg.getBytes().length];
            System.arraycopy(HEADER.getBytes(), 0, bbuffer, 0, HEADERLEN);
			System.arraycopy(msg.getBytes(), 0, bbuffer, HEADERLEN, msg.getBytes().length);
			send = new DatagramPacket(bbuffer, bbuffer.length, peerAddress.getAddress(), peerAddress.getPort());
			dsock.send(send);
		} catch (IOException io) {
			throw new RuntimeException(io);
		}
		lincoln.write("Sent Swiss message to associate.");

	}

	public String recvMessage() {

		String recvStr = null;
		byte[] mainBuffer = new byte[MAXMSGLEN], headerBuffer = new byte[HEADERLEN];

		try {
			receive = new DatagramPacket(headerBuffer, headerBuffer.length);
			dsock.receive(receive);
			System.arraycopy(receive.getData(), 0, mainBuffer, 0, HEADERLEN);
			if (Arrays.equals(mainBuffer, HEADER.getBytes()))
				recvStr = new String(receive.getData());
		} catch (AsynchronousCloseException | SocketException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
		lincoln.write("Received message from associate.");
        return recvStr;

	}

	public UserNetworkIdentifier getSTUN() {

		DatagramPacket messagePacket = null, responsePacket = null;
		int[] hdr = null;
		byte[] id = new byte[STUNIDLEN], message = new byte[STUNHDRLEN], response = new byte[256];
		byte[] translatedPort = new byte[2], translatedAddress = new byte[4];
		int which = 0, responseLength = 0, asciiPort = 0;
		String[] asciiAddress = new String[4];
		StringBuilder trueAddress = new StringBuilder();

		//construct STUN request
		hdr = new int[] {
				0x00, 0x01,
				0x00, 0x00,
				0x21, 0x12, 0xA4, 0x42
		};
		new SecureRandom().nextBytes(id);
		for (int i = 0; i < hdr.length; i++)
			message[i] = (byte) hdr[i];
		System.arraycopy(id, 0, message, hdr.length, id.length);
		responsePacket = new DatagramPacket(response, 0, response.length);
		//loop until we get a usable stun server, then test received packet identity
		while (true) {
			try {
				messagePacket = new DatagramPacket(message, 0, message.length, STUNSERVERS[which].getAddress(), STUNSERVERS[which].getPort());
				dsock.send(messagePacket);
				dsock.receive(responsePacket);
				lincoln.write("Usable STUN server identified.");
			} catch (IOException io) {
				if (which == STUNSERVERS.length - 1) {
					lincoln.write("No usable STUN server identified.");
					return null;
				} else
					++which;
				continue;
			}
			if (response[0] != 0x01 || response[1] != 0x01) {
				lincoln.write("Received improper response from STUN server.");
				++which;
			} else {
				lincoln.write("Received proper binding response from STUN server.");
				break;
			}
		}
		//format response
		responseLength = response[STUNHDRLEN + 2] << 8;
		responseLength |= response[STUNHDRLEN + 3];
		response = Arrays.copyOfRange(response, STUNHDRLEN + 4, STUNHDRLEN + 4 + responseLength);
		//parse XORed port
		translatedPort[0] = (byte) (response[2] ^ MAGICCOOKIE[0]);
		translatedPort[1] = (byte) (response[3] ^ MAGICCOOKIE[1]);
		//parse XORed address
		for (int i = 0; i < translatedAddress.length; i++)
			translatedAddress[i] = (byte) (response[i + 4] ^ MAGICCOOKIE[i]);
		asciiPort = ((translatedPort[0] & 0xFF) << 8) | (translatedPort[1] & 0xFF);
		for (int i = 0; i < asciiAddress.length; i++) {
			asciiAddress[i] = Integer.toString(translatedAddress[i] & 0xFF);
			trueAddress.append(asciiAddress[i]);
			trueAddress.append('.');
		}
		trueAddress.deleteCharAt(trueAddress.length() - 1);
		lincoln.write("STUN ip:port pair acquired: " + trueAddress.toString() +
				':' +
				asciiPort +
				'.');
		return new UserNetworkIdentifier(trueAddress.toString(), Integer.toString(asciiPort));

	}

	public InetAddress getPeerAddress() { return peerAddress.getAddress(); }

	public int getPeerPort() { return peerAddress.getPort(); }

	public void close() {

		dsock.close();
		lincoln.write("Closed Link.");

	}

	@Override
	public String toString() { return dsock.getLocalAddress().toString(); }

}
