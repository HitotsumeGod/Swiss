package client.util;

public final class UserNetworkIdentifier {

	private final String UNetID;

	public UserNetworkIdentifier(String address, String port) {

		StringBuilder sb = new StringBuilder();
		sb.append(address);
		sb.append(':');
		sb.append(port);
		UNetID = UserNetworkIdentifier.encrypt(sb.toString());

	}

	public UserNetworkIdentifier(String UNetID) {

		this.UNetID = UNetID;

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


		assert(!id.isEmpty());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < id.length(); i++)
			sb.append((int) id.charAt(i));
		return sb.toString();

	}

	public static String decrypt(UserNetworkIdentifier id) {

		assert(id != null);
		StringBuilder sb = new StringBuilder();
		StringBuilder second = new StringBuilder();
		for (int i = 0; i < id.UNetID.length(); i += 2) {
			second.append(id.UNetID.charAt(i));
			second.append(id.UNetID.charAt(i + 1));
			sb.append((char) Integer.parseInt(second.toString()));
			second.setLength(0);
		}
		return sb.toString();

	}

}
