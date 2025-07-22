package shared;

public class Encrypter {

    private final long seed;
    private final short code;

    public Encrypter() {

        seed = 2;
        code = 3;

    }

    public char[] encrypt(String s) {

        char[] encrypted = new char[s.length()];
        for (int i = 0; i < encrypted.length; i++)
            encrypted[i] = (char) (s.charAt(i) + seed);
        return encrypted;

    }

    public char[] encrypt(char[] arr) {

        char[] encrypted = new char[arr.length];
        for (int i = 0; i < arr.length; i++)
            encrypted[i] = (char) (arr[i] + seed);
        return encrypted;

    }

    public char[] decrypt(String s) {

        char[] decrypted = new char[s.length()];
        for (int i = 0; i < decrypted.length; i++)
            decrypted[i] = (char) (s.charAt(i) - seed);
        return decrypted;

    }

    public char[] decrypt(char[] arr) {

        char[] decrypted = new char[arr.length];
        for (int i = 0; i < decrypted.length; i++)
            decrypted[i] = (char) (arr[i] - seed);
        return decrypted;

    }

    public short getSeedCode() { return code; }
}
