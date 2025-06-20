package client.screen;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import client.net.OneWayLink;
import client.net.LinkHandler;
import client.net.TwoWayLink;
import shared.AssociateHandler;
import shared.Associate;
import shared.Logger;

public final class MenuOptionsHandler {

	private MenuOptionsHandler() {}

	public static OneWayLink initOneWayConnection(String opts, AssociateHandler assocHandler, LinkHandler lkHandler) {

		Associate assoc = null;
		OneWayLink link = null;

		Logger logger = new Logger("src/main/resources/menuoptionshandler.log", true);
		for (Associate a : assocHandler.getAssociates())
			if (a.getName().equals(opts)) {
				assoc = a;
				break;
			}
		if (assoc != null)
			link = new OneWayLink(assoc.getHost());
		if (link != null)
			logger.write("Connected to associate " + assoc.getName() + " on host " + assoc.getHost() + '.');
		return link;

	}

	public static TwoWayLink initTwoWayConnection(String opts, AssociateHandler assocHandler, LinkHandler lkHandler) {

		Associate assoc = null;
		TwoWayLink link = null;

		Logger logger = new Logger("src/main/resources/menuoptionshandler.log", true);
		for (Associate a : assocHandler.getAssociates())
			if (a.getName().equals(opts)) {
				assoc = a;
				break;
			}
		if (assoc == null)
			return null;
		while (true) {
			if ((link = new TwoWayLink(assoc.getHost())) == null)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			else
				break;
		}
		logger.write("Connected to associate " + assoc.getName() + " on host " + assoc.getHost() + '.');
		return link;

	}

	public static void performGetUNetID(String opts) {}

	public static void performAddAssociate(String opts) {}

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