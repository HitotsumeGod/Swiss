package daemon;

import shared.UserNetworkIdentifier;
import daemon.net.LinkListener;

public class Pilot {

	public static void main(String[] args) {

		LinkListener link;

		link = new LinkListener();
		link.getFellow();
		while (true)
			link.printStuff();

	}

}
