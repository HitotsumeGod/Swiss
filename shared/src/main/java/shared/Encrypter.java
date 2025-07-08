package shared;

public class Encrypter {

    private final long seed;
    private final short code;

    public Encrypter() {

        /*LocalTime ts = LocalTime.now();
        if (ts.getNano() % 2 == 0) {
            seed = 1520384710293847501L;
            code = 2;
        } else if (ts.getNano() % 3 == 0) {
            seed = 2738194032183409187L;
            code = 3;
        } else if (ts.getNano() % 5 == 0) {
            seed = 904182731092837465L;
            code = 5;
        } else if (ts.getNano() % 7 == 0) {
            seed = 6283918273645093812L;
            code = 7;
        } else {
            seed = 1328491738491273847L;
            code = 1;
        }*/
        seed = 2;
        code = 3;

    }

    public Encrypter(short seedCode) {

        code = seedCode;
        switch (seedCode) {
            case 2:
                seed = 1520384710293847501L;
                break;
            case 3:
                seed = 2738194032183409187L;
                break;
            case 5:
                seed = 904182731092837465L;
                break;
            case 7:
                seed = 6283918273645093812L;
                break;
            default:
                seed = 1328491738491273847L;
        }

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
