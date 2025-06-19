package client.net;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import shared.Link;
import shared.Logger;

public class TwoWayLink implements Link {

	private Logger logger;
	private Socket client = null;
	private ServerSocket server = null;
	private BufferedReader reader = null;
	private PrintWriter writer = null;

	public TwoWayLink(String hostname) {

		try {
			logger = new Logger("src/main/resources/twowaylink.log", true);
			server = new ServerSocket(PORT);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	@Override
	public boolean sendMessage(String msg) {

		writer.println(HEADER);
		writer.println(msg);
		writer.flush();
		return true;

	}

	@Override
	public boolean recvMessage() {

		try {
			if (!reader.ready()) {
				logger.write("Stream is not ready for reading.");
				return false;
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
		return true;

	}

	@Override
	public void close() {

		try {
			if (client != null)
				client.close();
			if (server != null)
				server.close();
			if (reader != null)
				reader.close();
			if (writer != null)
				writer.close();
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public boolean acceptConnection() {

		try {
			client = server.accept();
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream());
		} catch (IOException io) {
			io.printStackTrace();
		}
		return true;

	}

}