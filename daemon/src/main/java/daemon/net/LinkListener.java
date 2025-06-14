package daemon.net;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStreamReader;
import java.io.IOException;

public class LinkListener {

	private static final int PORT = 7777;
	private ServerSocket server = null;
	private Socket client = null;
	private InputStreamReader clientReader = null;

	public LinkListener() {

		try {
			server = new ServerSocket(PORT);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public boolean recover() {

		StringBuilder str;
		int c;

		try {
			if (client == null) {
				client = server.accept();
				clientReader = new InputStreamReader(client.getInputStream());
			} 
			if (!clientReader.ready())
				return false;
			str = new StringBuilder();
			while ((c = clientReader.read()) > 0)
				str.append((char) c);
			System.out.println(str.toString());
		} catch (IOException io) {
			io.printStackTrace();
		}
		return true;

	}

}
