package client.net;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

public class OneWayLink {

	private static final int PORT = 7777;
	private Socket link;
	private PrintWriter foucault;

	public OneWayLink(String hostname) {

		try {
			link = new Socket(hostname, PORT);
			foucault = new PrintWriter(link.getOutputStream(), true);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void sendMessage(String msg) {

		foucault.println(msg);

	}

	public void close() {

		try {
			foucault.close();
			link.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}



}