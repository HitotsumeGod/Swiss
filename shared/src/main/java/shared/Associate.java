package shared;

public class Associate {

	private final String username;
	private final UserNetworkIdentifier unetid;

	public Associate(String username, UserNetworkIdentifier unetid) {

		this.username = username;
		this.unetid = unetid;

	}

	public String getName() {

		return username;

	}

	public String getHost() {

		return UserNetworkIdentifier.decrypt(unetid);

	}

	public UserNetworkIdentifier getID() {

		return unetid;

	}

}