package client;

import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import client.net.OneWayLink;
import shared.Associate;

public class Pilot {

	private static final String HOST = "127.0.0.1";
	private static final String ASSOCIATEDBPATH = "src/main/resources/assocs.db";
	private static Associate[] associates = new Associate[20];

	public static void main(String[] args) {

		Scanner scan = null;
		OneWayLink link = null;
		Associate toContact;
		String assocName;

		toContact = null;
		scan = new Scanner(System.in);
		loadAssociates();
		for (Associate a : associates) {
			if (a == null)
				break;
			System.out.println(a.getHost());
		}
		System.out.println("Please enter the name of the associate you wish to contact.");
		assocName = scan.nextLine();
		for (Associate a : associates) 
			if (a.getName().equals(assocName)) {
				toContact = a;
				break;
			}
		if (toContact != null)
			link = new OneWayLink(toContact.getHost());		
		else
			return;
		while (true)
			link.sendMessage(scan.nextLine());

	}

	private static void loadAssociates() {

		BufferedReader reader;
		String lne;
		int n;

		n = 0;
		try {
			reader = new BufferedReader(new FileReader(new File(ASSOCIATEDBPATH)));
			while ((lne = reader.readLine()) != null)
				associates[n++] = new Associate(lne.substring(0, lne.indexOf('%')), lne.substring(lne.indexOf('%') + 1));
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

}
