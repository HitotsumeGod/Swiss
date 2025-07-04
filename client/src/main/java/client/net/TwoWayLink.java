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

	private Logger logger = null;
	private Socket sock;
	private ServerSocket server = null;
	private BufferedReader reader = null;
	private PrintWriter writer = null;

	public TwoWayLink(String hostname) {

		try {
			logger = new Logger("logs/twowaylink.log", false);
			try {
				sock = new Socket(hostname, PORT);
			} catch (IOException noConnect) {
				server = new ServerSocket(PORT);
				sock = server.accept();
			}
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			writer = new PrintWriter(sock.getOutputStream());
			logger.write("TwoWayLink established.");
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(1);
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
	public String recvMessage() {

		String recvStr = null;

		try {
			if (reader == null) {
				logger.write("Stream is not ready for reading.");
				return null;
			}
			if (reader.readLine().equals(HEADER))
				recvStr = reader.readLine();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return recvStr;

	}

	@Override
	public void close() {

		try {
			if (sock != null)
				sock.close();
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

	@Override
	public String toString() {

		return sock.getLocalAddress().toString();

	}

}
