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
import shared.AssociateHandler;

public class Pilot {

	private static final String HOST = "127.0.0.1";

	public static void main(String[] args) {

		Scanner scan = null;
		OneWayLink link = null;
		AssociateHandler handler;
		Associate toContact;
		String response;
		Screen s;

		toContact = null;
		scan = new Scanner(System.in);
		handler = new AssociateHandler();
		s = Screen.createMenuScreen(handler);
		s.updateScreen();

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
