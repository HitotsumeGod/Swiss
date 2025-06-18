package client.net;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;
import shared.Link;

public class OneWayLink implements Link {

	private Socket link;
	private PrintWriter foucault;

	public OneWayLink(String hostname) {

		try {
			link = new Socket(hostname, PORT);
			foucault = new PrintWriter(link.getOutputStream());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
		}

	}

	@Override
	public boolean sendMessage(String msg) {

		foucault.println(HEADER);
		foucault.println(msg);
		foucault.flush();
		return true;

	}

	@Override
	public boolean recvMessage() { return false; }

	@Override
	public void close() {

		try {
			foucault.close();
			link.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}



}