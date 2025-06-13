package net;

import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class OneWayLink {

	private static final int PORT = 7777;
	private Socket linkSocket = null;
	private OutputStreamWriter linkWriter = null;

	public OneWayLink(UserNetworkIdentifier id) {

		try {
			linkSocket = new Socket(UserNetworkIdentifier.translate(id), PORT);
			linkWriter = new OutputStreamWriter(linkSocket.getOutputStream());
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void sendMessage(String message) {

		try {
			if (!linkSocket.isConnected()) {
				System.out.println("Socket is NOT connected. Quitting!!!");
				System.exit(-1);
			}
			linkWriter.write(message, 0, message.length());
			linkWriter.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}

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
