package client.screen;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import client.net.OneWayLink;
import client.net.LinkHandler;
import shared.AssociateHandler;
import shared.Associate;
import shared.Logger;

public final class MenuOptionsHandler {

	private MenuOptionsHandler() {}

	public static void performOneWayConnection(String opts, ArrayList<Component> components, AssociateHandler assocHandler, LinkHandler lkHandler) {

		Logger logger = null;
		Associate assoc = null;
		OneWayLink link = null;
		JPanel panel = null;
		JTextField text = null;

		logger = new Logger("src/main/resources/menuoptionshandler.log", true);
		panel = (JPanel) components.get(1);
		for (Associate a : assocHandler.getAssociates())
			if (a.getName().equals(opts)) {
				assoc = a;
				break;
			}
		if (assoc != null)
			link = new OneWayLink(assoc.getHost());
		if (link != null)
			logger.write("Connected to associate " + assoc.getName() + " on host " + assoc.getHost() + "!");
		link.close();

	}

	public static void performGetUNetID(String opts) {}

	public static void performAddAssociate(String opts) {}

}