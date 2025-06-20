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
	private Socket myClient, theirClient;
	private ServerSocket server = null;
	private BufferedReader reader = null;
	private PrintWriter writer = null;
	private String recvStr = null;

	public TwoWayLink(String hostname) {

		try {
			logger = new Logger("src/main/resources/twowaylink.log", true);
			server = new ServerSocket(PORT);
			Thread t = new Thread(() -> {
				try {
					theirClient = server.accept();
				} catch (IOException io) {
					io.printStackTrace();
				}
			});
			t.start();
			myClient = new Socket(hostname, PORT);
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			reader = new BufferedReader(new InputStreamReader(theirClient.getInputStream()));
			writer = new PrintWriter(myClient.getOutputStream());
			logger.write("TwoWayLink established.");
		} catch (IOException io) {
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

		try {
			if (reader == null || !reader.ready()) {
				logger.write("Stream is not ready for reading.");
				return null;
			}
			recvStr = reader.readLine();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return recvStr;

	}

	@Override
	public void close() {

		try {
			if (myClient != null)
				myClient.close();
			if (theirClient != null)
				theirClient.close();
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

}
