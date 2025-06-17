package client;

import java.util.Scanner;
import shared.UserNetworkIdentifier;
import client.net.OneWayLink;

public class Pilot {

	public static final String HOST = "127.0.0.1";

	public static void main(String[] args) {

		Scanner scan = null;
		UserNetworkIdentifier uni = null;
		OneWayLink link = null;

		scan = new Scanner(System.in);
		uni = new UserNetworkIdentifier(HOST);
		link = new OneWayLink(uni);
		while (true)
			link.sendMessage(scan.nextLine());

	}

}
