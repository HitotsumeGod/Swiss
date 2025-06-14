package daemon;

import shared.UserNetworkInterface;
import daemon.net.LinkListener;

public class Pilot {

	private static final int PORT = 7777;

	public static void main(String[] args) {

		LinkListener link;

		link = new LinkListener();
		while (link.recover());

	}

}
