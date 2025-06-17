package daemon;

import java.io.File;
import shared.UserNetworkIdentifier;
import shared.Logger;
import daemon.net.LinkListener;

public class Pilot {

	public static void main(String[] args) {

		LinkListener link;
		Logger logger;

		link = new LinkListener();
		logger = new Logger(new File("src/main/resources/pilot.log"));
		link.getClient();
		while (link.getMessage());

	}

}
