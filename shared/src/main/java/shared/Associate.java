package shared;

public class Associate {

	private final String username;
	private final String hostname;

	public Associate(String username, String hostname) {

		this.username = username;
		this.hostname = hostname;

	}

	public String getName() {

		return username;

	}

	public String getHost() {

		return hostname;

	}

}