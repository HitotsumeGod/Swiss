package client.net;

import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import shared.Logger;
import shared.Link;

public class OneWayLink implements Link {

	private Logger logger;
	private Socket link;
	private BufferedWriter foucault;

	public OneWayLink(String hostname) {

		try {
			logger = new Logger("src/main/resources/onewaylink.log", false);
			link = new Socket(hostname, PORT);
			foucault = new BufferedWriter(new OutputStreamWriter(link.getOutputStream()));
		} catch (IOException io) {
			io.printStackTrace();
		}

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