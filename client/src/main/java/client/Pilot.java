package client;

import java.util.Scanner;
import java.util.ArrayList;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import client.net.OneWayLink;
import client.screen.Screen;
import shared.UserNetworkIdentifier;
import shared.Associate;

public class Pilot {

	private static final String HOST = "127.0.0.1";
	private static final String ASSOCIATEDBPATH = "src/main/resources/assocs.db";
	private static ArrayList<Associate> associateList = new ArrayList<>();

	public static void main(String[] args) {

		Scanner scan = null;
		OneWayLink link = null;
		Associate toContact;
		String response;
		Screen s;

		toContact = null;
		scan = new Scanner(System.in);
		loadAssociates();
		s = Screen.createMenuScreen();
		s.updateScreen();
		while (true) {
			response = scan.nextLine();
			switch (response) {
			case "CONNECT":
				System.out.println("Please enter the name of the associate you wish to contact.");
				for (Associate a : associateList) 
					System.out.print(a.getID() + " : " + a.getName() + " ---> " + a.getHost() + '\n');
				response = scan.nextLine();
				for (Associate a : associateList) 
					if (a.getName().equals(response)) {
						toContact = a;
						break;
					}
				if (toContact != null)
					link = new OneWayLink(toContact.getHost());	
				else
					return;
				System.out.println("Connected to associate \033[3m" + toContact.getName() + "\033[0m on host \033[3m" + toContact.getHost() + "!\033[0m");
				while (link.sendMessage(scan.nextLine()));
				break;
			case "GET":
				System.out.println("Please enter the name of the associate whose UNetID you wish to GET (press ENTER for your own).");
				response = scan.nextLine();
				if (response.length() < 1)
					System.out.println(UserNetworkIdentifier.encrypt(getMyHostIP()));
				else 
					for (Associate a : associateList)
						if (a.getName().equals(response)) {
							System.out.println(a.getName() + " ---> " + a.getID().toString());
							break;
						}
				break;
			case "ADD":
				addAssociate(scan.nextLine(), new UserNetworkIdentifier(scan.nextLine()));
				break;
			}
		}

	}

	private static void loadAssociates() {

		BufferedReader reader;
		String lne;
		int n;

		n = 0;
		try {
			reader = new BufferedReader(new FileReader(new File(ASSOCIATEDBPATH)));
			while ((lne = reader.readLine()) != null)
				associateList.add(new Associate(lne.substring(0, lne.indexOf('%')), new UserNetworkIdentifier(lne.substring(lne.indexOf('%') + 1))));
			reader.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	private static void addAssociate(String name, UserNetworkIdentifier unetid) {

		associateList.add(new Associate(name, unetid));

	}

	private static String getMyHostIP() {

		String ret;
		BufferedReader reader;
		URI uri;

		ret = null;
		uri = null;
		try {
			uri = new URI("http://ipecho.net/plain");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));
			ret = reader.readLine();
			reader.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return ret;

	}

}
