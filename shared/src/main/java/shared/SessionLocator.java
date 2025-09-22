package shared;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * The SessionLocator class serves as a peer-to-peer invitatation to a Swiss
 * communication session.
 * 
 * Format: 64-bit protocol header, 32-bit IP address, 16-bit port number, 8-bit options bitfield<p>
 * Total Length: 120 bits; 15 bytes<p>
 * A SessionLocator supports the following translations:<p>
 * String ---> SessionLocator (via SessionLocator(String s))<p>
 * SessionLocator ---> String (via session.toString())<p>
 */
public final class SessionLocator {

	private static final int FIXEDLENGTH = 15;
    private static final int VARIANCE = 67;
	private static final byte[] PROTOCOLHEADER = { 's', 'w', 'i', 's', 's', ':', '/', '/' };
    private static final byte ENCRYPTIONMASK = (byte) 0b10000000;
    private final InetSocketAddress sessionAddress;
    private byte sessionOptions = (byte) 0;

    public SessionLocator(String locatorString) {

        byte[] unobscured = new byte[locatorString.length()];
        byte[] byteAddress = new byte[4];

        for (int i = 0; i < unobscured.length; i++)
            unobscured[i] = (byte) (locatorString.charAt(i) - VARIANCE);
        System.arraycopy(unobscured, 0, byteAddress, 0, byteAddress.length);
        sessionAddress = new InetSocketAddress(InetAddress.getByAddress(byteAddress), (unobscured[12] << 8) | unobscured[13]);

    }

    public SessionLocator(InetSocketAddress address) { 
        
        sessionAddress = address;
    
    }

    public void enableEncryption() { }

    public boolean isEncryptionEnabled() { return false; }

    public InetSocketAddress getAddress() { return null; }

	public int length() { return FIXEDLENGTH; }

	@Override
	public String toString() { 
        
        byte[] locator = new byte[FIXEDLENGTH];
        byte[] obscured = new byte[FIXEDLENGTH];
        byte[] address = sessionAddress.getAddress().getAddress();
        int port = sessionAddress.getPort();

        System.arraycopy(locator, 0, PROTOCOLHEADER, 0, PROTOCOLHEADER.length);
        System.arraycopy(locator, PROTOCOLHEADER.length, address, 0, address.length);
        locator[PROTOCOLHEADER.length + address.length] = (byte) ((port >> 8) & 0x000000FF);
        locator[PROTOCOLHEADER.length + address.length + 1] = (byte) (port & 0x000000FF);
        locator[PROTOCOLHEADER.length + address.length + 1] = sessionOptions;
        for (int i = 0; i < obscured.length; i++)
            obscured[i] = (byte) (locator[i] + VARIANCE);
        return new String(obscured, StandardCharsets.UTF_8);
    
    }

}
