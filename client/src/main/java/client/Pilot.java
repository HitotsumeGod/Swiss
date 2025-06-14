package client;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import shared.UserNetworkIdentifier;
import client.net.OneWayLink;

public class Pilot {

	public static final String HOST = "192.168.0.224";

	public static void main(String[] args) {

		ExecutorService threadPool = null;
		Scanner scan = null;
		UserNetworkIdentifier uni = null;
		OneWayLink link = null;

		threadPool = Executors.newCachedThreadPool();
		scan = new Scanner(System.in);
		uni = new UserNetworkIdentifier(HOST);
		link = new OneWayLink(uni);
		threadPool.execute(runtime);
		while (true)
			link.sendMessage(scan.nextLine());

	}

}
