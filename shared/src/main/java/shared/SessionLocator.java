package shared;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

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

        BadSessionLocatorFormatException(String s) { super(s); }

    }

    private static final int FIXEDLENGTH = 15;
    private static final int VARIANCE = 7;
    private static final int PRINTRANGECEIL = 127;
    private static final int PRINTRANGEFLOOR = 32;
    private static final char DENOTEPOSITIVEVARIANCE = '<';
    private static final char DENOTENEGATIVEVARIANCE = '>';
    private static final char DENOTESECTION = '/';
    private static final char DENOTESHIFTDELIM = '+';
	private static final String PROTOCOLHEADER = "swiss://";
    private static final int ENCRYPTIONMASK = (int) 0b10000000;
    private final InetSocketAddress sessionAddress;
    private int sessionOptions = (int) 0;

    public SessionLocator(String locatorString) throws BadSessionLocatorFormatException {

        ArrayList<Character> locatorBytes = new ArrayList<>();
        ArrayList<Character> locatorInfo, locatorVariance, translatedLocatorInfo;

        for (int i = 0; i < locatorString.length(); i++)
            locatorBytes.add((char) (locatorString.charAt(i)));
        locatorBytes = unobscure(locatorBytes);
        byte[] addr = new byte[4];
        for (int i = 0; i < addr.length; i++)
            addr[i] = (byte) ((char) locatorBytes.get(i));
        int port = ((byte) ((char) locatorBytes.get(5))) << 8 | ((byte) ((char) locatorBytes.get(6)));
        for (byte b : addr)
            System.out.printf("%02X ", b);
        System.out.println();
        System.out.println(port);
        try {
            sessionAddress = new InetSocketAddress(InetAddress.getByAddress(addr), port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

    public SessionLocator(InetSocketAddress address) { sessionAddress = address; }

    /**
     * Obfuscates SessionLocator info data, and makes it human-readable,
     * in prepraration for being sent across a network as a string.
     * @param bytes An ArrayList of bytes containing an IPv4 address,
     *              computer port number, and options bitfield. The Swiss
     *              header should *not* be prepended.
     * @return An obfusctated ArrayList of bytes containing a header, info
     *              field, and shifts field, ready to be processed to a String
     */
    public ArrayList<Character> obscure(ArrayList<Character> bytes) {

        ArrayList<Character> obscured = new ArrayList<Character>();
        ArrayList<Character> shifts = new ArrayList<Character>();
        Random rand = new Random();
        int max = 20, min = 1;

        for (Character b : bytes)
            if (b < PRINTRANGEFLOOR) {
                System.out.printf("< %02X\n", (byte) (char) b);
                shifts.add((char) (PRINTRANGEFLOOR - b + (rand.nextInt(max - min) + min)));
                obscured.add(DENOTEPOSITIVEVARIANCE);
                obscured.add((char) (b + shifts.getLast()));
            } else if (b > PRINTRANGECEIL) {
                System.out.printf("> %02X\n", (byte) (char) b);
                shifts.add((char) (b - PRINTRANGECEIL + (rand.nextInt(max - min) + min)));
                obscured.add(DENOTENEGATIVEVARIANCE);
                obscured.add((char) (b - shifts.getLast()));
            } else
                obscured.add(b);
        obscured.add('/');
        ArrayList<String> shiftStrings = new ArrayList<>();
        for (Character c : shifts)
            shiftStrings.add(Integer.toString((int) ((char) c) & 0xFF));
        for (String s : shiftStrings) {
            for (int i = 0; i < s.length(); i++)
                obscured.add(s.charAt(i));
            obscured.add(DENOTESHIFTDELIM);
        }
        return obscured;

    }

    public ArrayList<Character> unobscure(ArrayList<Character> bytes) throws BadSessionLocatorFormatException {

        ArrayList<Character> unobscured = new ArrayList<>();
        ArrayList<Character> temp = new ArrayList<>();
        ArrayList<Character> info, shifts;
        ArrayList<String> shiftStrings = new ArrayList<>();

        for (int i = 0; i < PROTOCOLHEADER.length(); i++)
            if (bytes.get(i) != PROTOCOLHEADER.charAt(i))
                throw new BadSessionLocatorFormatException("header malformed");
        bytes = (ArrayList<Character>) bytes.subList(PROTOCOLHEADER.length(), bytes.size());
        info = (ArrayList<Character>) bytes.subList(0, bytes.indexOf('/'));
        shifts = (ArrayList<Character>) bytes.subList(bytes.indexOf('/') + 1, bytes.size());
        for (int i = 0, lastDelim = 0; i < shifts.size(); i++)
            if (shifts.get(i) == DENOTESHIFTDELIM) {
                char[] tempArray = new char[i];
                for (int ii = ++lastDelim; ii < i; ii++)
                    tempArray[ii] = shifts.get(ii);
                shiftStrings.add(new String(tempArray));
                lastDelim = i;
            }
        for (int i = 0, ii = 0; i < info.size(); i++)
            if (info.get(i) == DENOTEPOSITIVEVARIANCE)
                temp.add((char) (info.get(i++ + 1) + Integer.parseInt(shiftStrings.get(ii))));
            else if (info.get(i) == DENOTENEGATIVEVARIANCE)
                temp.add((char) (info.get(i++ + 1) - Integer.parseInt(shiftStrings.get(ii))));
            else if (info.get(i) == DENOTESECTION)
                break;
            else
                temp.add(info.get(i));
        return temp;

    }

    public void enableEncryption() { sessionOptions |= ENCRYPTIONMASK; }

    public boolean isEncryptionEnabled() { return (sessionOptions & ENCRYPTIONMASK) == ENCRYPTIONMASK; }

    public InetSocketAddress getAddress() { return sessionAddress; }

	public int length() { return FIXEDLENGTH; }

	@Override
	public String toString() {

        ArrayList<Character> toObscure = new ArrayList<>();
        StringBuilder bob = new StringBuilder();

        for (byte b : sessionAddress.getAddress().getAddress())
            toObscure.add((Character) ((char) b));
        toObscure.add((char) (sessionAddress.getPort() >> 8));
        toObscure.add((char) sessionAddress.getPort());
        toObscure.add((Character) (char) sessionOptions);
        toObscure = obscure(toObscure);
        bob.append(PROTOCOLHEADER);
        for (Character c : toObscure)
            bob.append(c);
        return bob.toString();
    
    }

}
