package shared;

public interface Link {

	String HEADER = "SWISS V1 ***000***";
	int PORT = 7777;

	boolean sendMessage(String msg);
	boolean recvMessage();
	void close();

}