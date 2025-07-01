package client.net;

import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import shared.Logger;
import shared.Link;

public class OneWayLink implements Link {

	private Logger logger = null;
	private Socket link = null;
	private BufferedWriter foucault = null;

	public OneWayLink(String hostname) {

		try {
			logger = new Logger("logs/onewaylink.log", false);
			link = new Socket(hostname, PORT);
			foucault = new BufferedWriter(new OutputStreamWriter(link.getOutputStream()));
		} catch (IOException io) {
			logger.write("IOException when creating socket.");
		}

	}

	public boolean checkState() {

		if (logger == null || link == null || foucault == null)
			return false;
		return true;

	}

	@Override
	public boolean sendMessage(String msg) {

		try {
			foucault.write(HEADER, 0, HEADER.length());
			foucault.write('\n');
			foucault.write(msg, 0, msg.length());
			foucault.write('\n');
			foucault.flush();
		} catch (IOException io) {
			logger.write("IOException when writing to linked host.");
			return false;
		}
		return true;

	}

	@Override
	public String recvMessage() { return null; }

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