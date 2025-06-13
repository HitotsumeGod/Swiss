package net;

public final class UserNetworkIdentifier {

	private String UNetID;

	public UserNetworkIdentifier(String id) {

		StringBuilder res = new StringBuilder();

		for (int i = id.length() - 1; i > 0; i--)
			res.append(id.charAt(i) + 20);
		UNetID = res.toString();

	}

	public int length() {

		return UNetID.length();

	}

	public char charAt(int index) {

		return UNetID.charAt(index);

	}

	public static String translate(UserNetworkIdentifier id) {

		StringBuilder res = new StringBuilder();

		for (int i = id.length() - 1; i > 0; i--)
			res.append(id.charAt(i) - 20);
		return res.toString();

	}

}