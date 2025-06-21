package daemon;

import shared.UserNetworkIdentifier;
import shared.Logger;
import daemon.net.LinkListener;

public class Pilot {

	public static void main(String[] args) {

		LinkListener link;
		Logger logger;

		link = new LinkListener();
		logger = new Logger("src/main/resources/pilot.log", true);
		link.getClient();
		while (link.getMessage());

	}

}
