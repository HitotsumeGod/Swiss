package client.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import client.net.TwoWayLink;
import shared.AssociateHandler;
import shared.Associate;
import shared.Logger;

public final class MyStaticMethods {

	private MyStaticMethods() {}

	public static TwoWayLink initTwoWayConnection(String opts, AssociateHandler assocHandler) {

		Associate assoc = null;
		TwoWayLink link = null;

		Logger logger = new Logger("logs/menuoptionshandler.log", false);
		for (Associate a : assocHandler.getAssociates())
			if (a.getName().equals(opts)) {
				assoc = a;
				break;
			}
		if (assoc == null)
			return null;
		link = new TwoWayLink(assoc.getHost());
		logger.write("Connected to associate " + assoc.getName() + " on host " + assoc.getHost() + '.');
		return link;

	}

	public static TwoWayLink initTwoWayConnection(String opts, AssociateHandler assocHandler, DatagramSocket socket) {

		Associate assoc = null;
		TwoWayLink link = null;

		Logger logger = new Logger("logs/menuoptionshandler.log", false);
		for (Associate a : assocHandler.getAssociates())
			if (a.getName().equals(opts)) {
				assoc = a;
				break;
			}
		if (assoc == null)
			return null;
		link = new TwoWayLink(assoc.getHost(), socket);
		logger.write("Connected to associate " + assoc.getName() + " on host " + assoc.getHost() + '.');
		return link;

	}

	public static String getMyHostIP() {

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

	public static String getMyLANIP() {

        try {
			String address = InetAddress.getLocalHost().toString();
			return address.substring(address.indexOf("/") + 1);
        } catch (UnknownHostException e) {
			throw new RuntimeException(e);
        }

    }

}