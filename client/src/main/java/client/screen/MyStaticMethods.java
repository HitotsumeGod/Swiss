package client.screen;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import client.util.UserNetworkIdentifier;
import client.util.Logger;

public final class MyStaticMethods {

	private static final int PORT = 23892;
	private static final int STUNPORT = 19302;
	private static final int STUNHDRLEN = 20;
	private static final int[] MAGICCOOKIE = { 0x21, 0x12, 0xA4, 0x42 };
	private static final String[]  stunServers = new String[] {
			"stun.l.google.com",
	};

	private MyStaticMethods() {}

	public static UserNetworkIdentifier getSTUN(DatagramSocket socket) {

		DatagramPacket messagePacket = null;
		DatagramPacket responsePacket = null;
		int[] tempFirstHalf = null;
		byte[] firstHalf = null;
		byte[] transID = new byte[12];
		byte[] message = null;
		byte[] response = new byte[256];
		byte[] translatedPort = new byte[2], translatedAddress = new byte[4];
		int which = 0, responseLength = 0;
		int asciiPort = 0;
		String[] asciiAddress = new String[4];
		StringBuilder trueAddress = new StringBuilder();
		Logger lincoln = new Logger("logs/getstun.log", false);

		//construct STUN request
		new SecureRandom().nextBytes(transID);
		tempFirstHalf = new int[] {
				0x00, 0x01,
				0x00, 0x00,
				0x21, 0x12, 0xA4, 0x42
		};
		firstHalf = new byte[tempFirstHalf.length];
		for (int i = 0; i < tempFirstHalf.length; i++)
			firstHalf[i] = (byte) tempFirstHalf[i];
		message = new byte[firstHalf.length + transID.length];
		int il = 0;
		for (; il < firstHalf.length; il++)
			message[il] = firstHalf[il];
		for (; il < transID.length; il++)
			message[il] = transID[il];

		responsePacket = new DatagramPacket(response, 0, response.length);
		//loop until we get a usable stun server, then test received packet identity
		while (true) {
			try {
				messagePacket = new DatagramPacket(message, 0, message.length, InetAddress.getByName(stunServers[which]), STUNPORT);
				socket.send(messagePacket);
				socket.receive(responsePacket);
			} catch (IOException io) {
				if (which == stunServers.length - 1) {
					lincoln.write("No usable STUN server found.");
					return null;
				} else
					++which;
				continue;
			}
			if (response[0] != 0x01 || response[1] != 0x01) {
				lincoln.write("Received improper response from STUN server.");
				++which;
			} else
				break;
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
		return new UserNetworkIdentifier(trueAddress.toString(), Integer.toString(asciiPort));

	}

	public static String getMyHostIP() {

		String ret;
		BufferedReader reader;
		URI uri;

		ret = null;
		uri = null;
		try {
			uri = new URI("http://ipecho.net/plain");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));
			ret = reader.readLine();
			reader.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return ret;

	}

	public static String getMyLANIP() {

        try {
			String address = InetAddress.getLocalHost().toString();
			return address.substring(address.indexOf("/") + 1);
        } catch (UnknownHostException e) {
			throw new RuntimeException(e);
        }

    }

}