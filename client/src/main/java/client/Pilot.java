package client;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import client.screen.Screen;
import shared.AssociateHandler;
import shared.Logger;

public class Pilot {

	private static final String HOST = "127.0.0.1";

	public static void main(String[] args) {

		Screen s = Screen.createMenuScreen(new AssociateHandler());
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
