package client.net;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;
import shared.UserNetworkIdentifier;

public class OneWayLink {

	private static final int PORT = 7777;
	private Socket linkSocket = null;
	private PrintWriter linkWriter = null;

	public OneWayLink(UserNetworkIdentifier id) {

		try {
			System.out.println(UserNetworkIdentifier.translate(id));
			linkSocket = new Socket(UserNetworkIdentifier.translate(id), PORT);
			linkWriter = new PrintWriter(linkSocket.getOutputStream());
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void sendMessage(String message) {

		if (!linkSocket.isConnected()) {
			System.out.println("Socket is NOT connected. Quitting!!!");
			System.exit(-1);
		}
		linkWriter.write(message);
		linkWriter.flush();

	}

	public void closeLink() {

		try {
			linkWriter.close();
			linkSocket.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

}
