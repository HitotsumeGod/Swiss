package shared;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * The SessionLocator class serves as a peer-to-peer invitatation to a Swiss
 * communication session.
 *<p>
 * Format: 64-bit protocol header, 32-bit IP address, 16-bit port number, 8-bit options bitfield<p>
 * Total Length: 120 bits; 15 bytes<p>
 * A SessionLocator supports the following translations:<p>
 * String ---> SessionLocator (via SessionLocator(String s))<p>
 * SessionLocator ---> String (via session.toString())<p>
 */
public final class SessionLocator {

    public final static class BadSessionLocatorFormatException extends Exception {

        BadSessionLocatorFormatException() { super("Some aspect of the provided session locator was malformed."); }

    }

    private static final int FIXEDLENGTH = 15;
    private static final int VARIANCE = 7;
	private static final byte[] PROTOCOLHEADER = { 's', 'w', 'i', 's', 's', ':', '/', '/' };
    private static final byte ENCRYPTIONMASK = (byte) 0b10000000;
    private final InetSocketAddress sessionAddress;
    private byte sessionOptions = (byte) 0;

    public SessionLocator(String locatorString) throws BadSessionLocatorFormatException {

        byte[] locatorBytes = locatorString.getBytes();
        byte[] header = new byte[PROTOCOLHEADER.length], address = new byte[4];

        if (locatorBytes.length < FIXEDLENGTH)
            throw new BadSessionLocatorFormatException();
        System.arraycopy(locatorBytes, 0, header, 0, header.length);
        if (!Arrays.equals(header, PROTOCOLHEADER))
            throw new BadSessionLocatorFormatException();
        System.arraycopy(locatorBytes, PROTOCOLHEADER.length, address, 0, address.length);
        try {
            sessionAddress = new InetSocketAddress(InetAddress.getByAddress(address), (locatorBytes[12] << 8) | locatorBytes[13]);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

    public SessionLocator(InetSocketAddress address) { sessionAddress = address; }

    public void enableEncryption() { sessionOptions |= ENCRYPTIONMASK; }

    public boolean isEncryptionEnabled() { return (sessionOptions & ENCRYPTIONMASK) == ENCRYPTIONMASK; }

    public InetSocketAddress getAddress() { return sessionAddress; }

	public int length() { return FIXEDLENGTH; }

	@Override
	public String toString() { 
        
        byte[] locator = new byte[FIXEDLENGTH];
        byte[] address = sessionAddress.getAddress().getAddress();
        int port = sessionAddress.getPort();

        System.arraycopy(PROTOCOLHEADER, 0, locator, 0, PROTOCOLHEADER.length);
        System.arraycopy(address, 0, locator, PROTOCOLHEADER.length, address.length);
        locator[PROTOCOLHEADER.length + address.length] = (byte) ((port >> 8) & 0x000000FF);
        locator[PROTOCOLHEADER.length + address.length + 1] = (byte) (port & 0x000000FF);
        locator[PROTOCOLHEADER.length + address.length + 2] = sessionOptions;
        for (int i = PROTOCOLHEADER.length; i < locator.length; i++)
            locator[i] -= VARIANCE;
        return new String(locator, StandardCharsets.UTF_8);
    
    }

}
