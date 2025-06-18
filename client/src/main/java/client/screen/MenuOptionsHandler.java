package client.screen;

import java.util.ArrayList;
import shared.AssociateHandler;
import shared.Associate;

public final class MenuOptionsHandler {

	private MenuOptionsHandler() {}

	public static void processCONNECT(String opts, AssociateHandler handler) {

		for (Associate a : handler.getAssociates()) 
			System.out.print(a.getID() + " : " + a.getName() + " ---> " + a.getHost() + '\n');

	}

	public static void processGET(String opts) {}

	public static void processADD(String opts) {}

}