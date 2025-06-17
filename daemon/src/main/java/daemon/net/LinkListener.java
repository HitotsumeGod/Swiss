package daemon.net;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class LinkListener {

	private static final int PORT = 7777;
	private ServerSocket server = null;
	private Socket client = null;
	private BufferedReader clientReader = null;

	public LinkListener() {

		try {
			server = new ServerSocket(PORT);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void getFellow() {

		try {
			client = server.accept();
			clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void printStuff() {

		try {
			System.out.println(clientReader.readLine());
		} catch (IOException io) {
			io.printStackTrace();
		}
		
	}

}
