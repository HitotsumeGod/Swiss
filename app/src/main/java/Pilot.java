import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import net.UserNetworkIdentifier;
import net.OneWayLink;
import net.LinkListener;
import net.ServerRuntime;

public class Pilot {

	public static final String HOST = "192.168.0.224";

	public static void main(String[] args) {

		ExecutorService threadPool = null;
		Scanner scan = null;
		UserNetworkIdentifier uni = null;
		LinkListener listener = null;
		OneWayLink link = null;
		ServerRuntime runtime = null;

		threadPool = Executors.newCachedThreadPool();
		scan = new Scanner(System.in);
		uni = new UserNetworkIdentifier(HOST);
		listener = new LinkListener();
		link = new OneWayLink(uni);
		runtime = new ServerRuntime(listener);
		threadPool.execute(runtime);
		while (true)
			link.sendMessage(scan.nextLine());

	}

}