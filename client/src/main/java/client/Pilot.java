package client;

import java.util.Scanner;
import client.net.OneWayLink;

public class Pilot {

	public static final String HOST = "127.0.0.1";

	public static void main(String[] args) {

		Scanner scan = null;
		OneWayLink link = null;

		scan = new Scanner(System.in);
		link = new OneWayLink(HOST);
		while (true)
			link.sendMessage(scan.nextLine());

	}

}
