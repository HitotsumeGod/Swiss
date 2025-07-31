package client.net;

public interface Link {

	String HEADER = "SWISS V1 ***000***";

	boolean sendMessage(String msg);
	String recvMessage();
	void close();

}