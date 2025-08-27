package swiss.util;

public class Encrypter {

    private final long seed;
    private final short code;

    public Encrypter() {

        seed = 2;
        code = 3;

    }

    public String encrypt(String s) {

        char[] encrypted = new char[s.length()];
        for (int i = 0; i < encrypted.length; i++)
            encrypted[i] = (char) (s.charAt(i) + seed);
        return new String(encrypted);

    }

    public String encrypt(char[] arr) {

        char[] encrypted = new char[arr.length];
        for (int i = 0; i < arr.length; i++)
            encrypted[i] = (char) (arr[i] + seed);
        return new String(encrypted);

    }

    public String decrypt(String s) {

        char[] decrypted = new char[s.length()];
        for (int i = 0; i < decrypted.length; i++)
            decrypted[i] = (char) (s.charAt(i) - seed);
        return new String(decrypted);

    }

    public String decrypt(char[] arr) {

        char[] decrypted = new char[arr.length];
        for (int i = 0; i < decrypted.length; i++)
            decrypted[i] = (char) (arr[i] - seed);
        return new String(decrypted);

    }

    public short getSeedCode() { return code; }
}
