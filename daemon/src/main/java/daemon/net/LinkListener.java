package daemon.net;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import shared.Logger;
import shared.Link;

public class LinkListener {

	private final Logger logger;
	private ServerSocket server = null;
	private Socket client = null;
	private BufferedReader clientReader = null;

	public LinkListener() {

		try {
			server = new ServerSocket(Link.PORT);
		} catch (IOException io) {
			io.printStackTrace();
		}
		logger = new Logger(new File("src/main/resources/linklistener.log"));

	}

	public boolean getClient() {

		try {
			client = server.accept();
			clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException io) {
			io.printStackTrace();
		}
		return true;

	}

	public boolean getMessage() {

		if (client == null) {
			logger.write("No available client!");
			return false;
		}
		try {
			if (clientReader.readLine().equals(Link.HEADER))
				System.out.println(clientReader.readLine());
		} catch (IOException io) {
			io.printStackTrace();
		}
		return true;

	}

}
