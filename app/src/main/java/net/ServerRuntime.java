package net;

public class ServerRuntime implements Runnable {

	private final LinkListener listener;

	public ServerRuntime(LinkListener listener) {

		this.listener = listener;

	}

	public void run() {

		while (listener.recover());

	}

}