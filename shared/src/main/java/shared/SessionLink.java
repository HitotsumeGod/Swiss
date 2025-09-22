package shared;

import java.util.StringTokenizer;
import java.nio.charset.StandardCharsets;

/**
 * The SessionLink class serves as a peer-to-peer invitatation to a Swiss
 * communication session.
 * Format: 64-bit protocol header, 32-bit IP address, 16-bit port number, 8-bit options bitfield
 * Total Length: 120 bits; 15 bytes
 */
public final class SessionLink {

	private static final int fixedLength = 15;
	private static final byte[] protocolHeader = { 's', 'w', 'i', 's', 's', ':', '/', '/' };
	private final byte[] sessionLink;

	public SessionLink(String address, short port) {

		StringTokenizer tokenEngine;

		sessionLink = new byte[fixedLength];
		tokenEngine = new StringTokenizer(address, ".");
		System.arraycopy(sessionLink, 0, protocolHeader, 0, protocolHeader.length);
		sessionLink[8] = Byte.parseByte(tokenEngine.nextToken());
		sessionLink[9] = Byte.parseByte(tokenEngine.nextToken());
		sessionLink[10] = Byte.parseByte(tokenEngine.nextToken());
		sessionLink[11] = Byte.parseByte(tokenEngine.nextToken());
		if (tokenEngine.hasMoreTokens())
			throw new RuntimeException();
		sessionLink[12] = (byte) (port >> 8);
		sessionLink[13] = (byte) (port | 0x00FF);
		sessionLink[14] = (byte) 0;

	}

	public SessionLink(String UNetID) {

		this.sessionLink = UNetID;

	}

	public int length() {

		return sessionLink.length();

	}

	@Override
	public String toString() {

		return new String(sessionLink, StandardCharsets.UTF_8);

	}

	public static String decrypt(SessionLink id) {

		StringBuilder sb = new StringBuilder();
		StringBuilder second = new StringBuilder();
		for (int i = 0; i < id.sessionLink.length(); i += 2) {
			second.append(id.sessionLink.charAt(i));
			second.append(id.sessionLink.charAt(i + 1));
			sb.append((char) Integer.parseInt(second.toString()));
			second.setLength(0);
		}
		return sb.toString();

	}

}
