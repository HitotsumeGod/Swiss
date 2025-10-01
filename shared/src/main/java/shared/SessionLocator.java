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

    private static final int PRINTRANGECEIL = 126;
    private static final int PRINTRANGEFLOOR = 32;
    private static final char DENOTEPOSITIVEVARIANCE = '<';
    private static final char DENOTENEGATIVEVARIANCE = '>';
    private static final char DENOTESECTION = '/';
    private static final char DENOTESHIFTDELIM = '+';
	private static final String PROTOCOLHEADER = "swiss://";
    private static final int ENCRYPTIONMASK = (int) 0b10000000;
    private final InetSocketAddress sessionAddress;
    private int sessionOptions = 0;

    public SessionLocator(String locatorString) throws BadSessionLocatorFormatException {

        ArrayList<Character> locatorBytes = new ArrayList<>();
        ArrayList<Character> locatorInfo = new ArrayList<>();
        ArrayList<Integer> locatorVariance = new ArrayList<>();
        ArrayList<Character> translatedLocatorInfo = new ArrayList<>();

        //parse string in character array, skipping header
        for (int i = locatorString.indexOf(DENOTESECTION) + 2; i < locatorString.length(); i++)
            locatorBytes.add(locatorString.charAt(i));
        //parse info into array
        for (int i = 0; i < locatorBytes.indexOf(DENOTESECTION); i++)
            locatorInfo.add(locatorBytes.get(i));
        //parse shifts into integers
        StringBuilder temp = new StringBuilder();
        for (int i = locatorBytes.indexOf(DENOTESECTION) + 1; i < locatorBytes.size(); i++)
            if (locatorBytes.get(i) == DENOTESHIFTDELIM)
                locatorVariance.add(Integer.valueOf(temp.toString()));
            else
                temp.append(locatorBytes.get(i));
        translatedLocatorInfo = unobscure(locatorInfo, locatorVariance);
        byte[] addr = new byte[4];
        for (int i = 0; i < addr.length; i++) {
            addr[i] = (byte) ((char) translatedLocatorInfo.get(i));
        }
        int port = ((byte) ((char) translatedLocatorInfo.get(4))) << 8 | ((byte) ((char) translatedLocatorInfo.get(5)));
        try {
            sessionAddress = new InetSocketAddress(InetAddress.getByAddress(addr), port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        System.out.println(sessionAddress.getAddress().toString());
        System.out.println(sessionAddress.getPort());

    }

    public SessionLocator(InetSocketAddress address) { sessionAddress = address; }

    /**
     * Obfuscates SessionLocator info data, and makes it human-readable,
     * in prepraration for being sent across a network as a string.
     *
     * This method *does not* assemble shifts into strings, but rather
     * appends them in their integral forms. Parsing and conversion
     * to String form should be handled by this class's toString().
     * @param bytes An ArrayList of bytes containing an IPv4 address,
     *              computer port number, and options bitfield. The Swiss
     *              header should *not* be prepended.
     * @return An obfusctated ArrayList of bytes containing a header, info
     *              field, and shifts field, ready to be processed to a String
     */
    public ArrayList<Character> obscure(ArrayList<Character> bytes) {

        ArrayList<Character> obscured = new ArrayList<>();
        ArrayList<Character> shifts = new ArrayList<>();
        Random rand = new Random();
        int max = 20, min = 1;

        for (Character b : bytes)
            if (b < PRINTRANGEFLOOR) {
                shifts.add((char) (PRINTRANGEFLOOR - b + (rand.nextInt(max - min) + min)));
                obscured.add(DENOTEPOSITIVEVARIANCE);
                obscured.add((char) (b + shifts.getLast()));
            } else if (b > PRINTRANGECEIL) {
                shifts.add((char) (b - PRINTRANGECEIL + (rand.nextInt(max - min) + min)));
                obscured.add(DENOTENEGATIVEVARIANCE);
                obscured.add((char) (b - shifts.getLast()));
            } else
                obscured.add(b);
        obscured.add('/');
        obscured.addAll(shifts);
        return obscured;

    }

    public ArrayList<Character> unobscure(ArrayList<Character> info, ArrayList<Integer> shifts) {

        ArrayList<Character> translated = new ArrayList<>();
        char c;

        for (int i = 0, ii = 0; i < info.size(); i++) {
            c = info.get(i);
            if (c == DENOTENEGATIVEVARIANCE)
                info.set(i + 1, (char) (info.get(i + 1) + shifts.get(ii++)));
            else if (c == DENOTEPOSITIVEVARIANCE)
                info.set(i + 1, (char) (info.get(i + 1) - shifts.get(ii++)));
            else
                translated.add(c);
        }
        return translated;
    }

    public void enableEncryption() { sessionOptions |= ENCRYPTIONMASK; }

    public boolean isEncryptionEnabled() { return (sessionOptions & ENCRYPTIONMASK) == ENCRYPTIONMASK; }

    public InetSocketAddress getAddress() { return sessionAddress; }

    //TODO: FIX
	public int length() { return -1; }

	@Override
	public String toString() {

        ArrayList<Character> toObscure = new ArrayList<>();
        ArrayList<String> shiftStrings = new ArrayList<>();
        StringBuilder bob = new StringBuilder();

        for (byte b : sessionAddress.getAddress().getAddress())
            toObscure.add((Character) ((char) b));
        toObscure.add((char) (sessionAddress.getPort() >> 8));
        toObscure.add((char) (sessionAddress.getPort() & 0xFF));
        toObscure.add((Character) (char) sessionOptions);
        toObscure = obscure(toObscure);
        for (int i = toObscure.indexOf(DENOTESECTION) + 1; i < toObscure.size(); i++)
            shiftStrings.add(Integer.toString((int) ((char) toObscure.get(i)) & 0xFF));
        bob.append(PROTOCOLHEADER);
        for (int i = 0; i <= toObscure.indexOf(DENOTESECTION); i++)
            bob.append(toObscure.get(i));
        for (String s : shiftStrings) {
            bob.append(s);
            bob.append(DENOTESHIFTDELIM);
        }
        return bob.toString();
    
    }

}
