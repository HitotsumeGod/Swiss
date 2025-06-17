package shared;

public final class UserNetworkIdentifier {

	private String UNetID;

	public UserNetworkIdentifier(String id) {

		UNetID = id;

	}

	public int length() {

		return UNetID.length();

	}

	public char charAt(int index) {

		return UNetID.charAt(index);

	}

	public static String translate(UserNetworkIdentifier id) {

		return id.UNetID;

	}

}
