package client.screen;

import java.util.ArrayList;
import client.net.OneWayLink;
import client.net.LinkHandler;
import shared.AssociateHandler;
import shared.Associate;

public final class MenuOptionsHandler {

	private MenuOptionsHandler() {}

	public static void processCONNECT(String opts, AssociateHandler assocHandler, LinkHandler lkHandler) {

		Associate abso = null;
		OneWayLink link = null;

		for (Associate a : assocHandler.getAssociates()) 
			if (a.getName().equals(opts)) {
				abso = a;
				break;
			}
		if (abso != null)
			link = new OneWayLink(abso.getHost());
		if (link != null)
			System.out.println("Connected to associate \033[3m" + abso.getName() + "\033[0m on host \033[3m" + abso.getHost() + "!\033[0m");
		link.close();

	}

	public static void processGET(String opts) {}

	public static void processADD(String opts) {}

}