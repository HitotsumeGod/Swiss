package shared;

public final class UserNetworkIdentifier {

	String UNetID;

	public UserNetworkIdentifier(String id) {

		UNetID = UserNetworkIdentifier.encrypt(id);

	}

	public int length() {

		return UNetID.length();

	}

	public char charAt(int index) {

		return UNetID.charAt(index);

	}

	@Override
	public String toString() {

		return UNetID;

	}

	public static String encrypt(String id) {

		StringBuilder sb;

		sb = new StringBuilder();
		for (int i = 0; i < id.length(); i++) {
			sb.append((int) id.charAt(i));
			//System.out.println("Char " + id.charAt(i) + " ---> " + (int) id.charAt(i));
		}
		return sb.toString();

	}

	public static String decrypt(UserNetworkIdentifier id) {

		StringBuilder sb, second;
		String sus;
		int checking;

		sb = new StringBuilder();
		second = new StringBuilder();
		for (int i = 0; i < id.UNetID.length(); i += 2) {
			second.append(id.UNetID.charAt(i));
			second.append(id.UNetID.charAt(i + 1));
			sb.append((char) (checking = Integer.valueOf(second.toString())));
			second.setLength(0);
			//System.out.println("Int " + checking + " ---> " + (char) checking);
		}
		return sb.toString();

	}

}
