package Swiss;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import shared.SessionLocator;
import shared.net.Link;
import shared.net.Conversation;

public class Pilot {

    private static Conversation chatter;

    public static void main(String[] args) {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String got;

        System.out.println("Welcome to the Swiss P2P Messenger!");
        System.out.println("To initiate a session with an associate, please input CONNECT.");
        try {
            got = input.readLine();
            if (got.equalsIgnoreCase("connect")) {
                String title = "Them";
                ExecutorService pool = Executors.newCachedThreadPool();
                Link link = new Link();
                SessionLocator loc = null;
                System.out.println("To start a session with an associate located on your current network, input LAN.");
                System.out.println("To start a session with an associate located on a remote network, input REMOTE.");
                got = input.readLine();
                if (got.equalsIgnoreCase("LAN"))
                    loc = new SessionLocator(new InetSocketAddress(InetAddress.getLocalHost(), Link.BINDPORT));
                else if (got.equalsIgnoreCase("REMOTE"))
                    loc = new SessionLocator(link.querySTUNServer());
                else
                    throw new RuntimeException("TODO fix this placeholder exception");
                System.out.printf("%s%s\n", "Your session locator is : ", loc.toString());
                System.out.println("Please provide your associate's session locator.");
                try {
                    link.setPeer(new SessionLocator(input.readLine()));
                } catch (SessionLocator.BadSessionLocatorFormatException e) {
                    throw new RuntimeException(e);
                }
                Future<?> sessionEstablished = pool.submit(() -> chatter = new Conversation(link));
                int dots = 3;
                while (!sessionEstablished.isDone()) {
                    System.out.print("Waiting for associate to join the session");
                    for (int i = 0; i < dots; i++) {
                        System.out.print('.');
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    dots++;
                    System.out.println();
                }
                System.out.println("Connected to associate.");
                Future<?> sessionContinuing = pool.submit(() -> {
                    String received;
                    while (true) {
                        if ((received = chatter.listen()) != null) {
                            System.out.println(title + " : " + received);
                        } else {
                            chatter.end();
                            System.out.println("An error occurred. The session has been broken.");
                            break;
                        }
                    }
                });
                while (!sessionContinuing.isDone()) {
                    if ((got = input.readLine()).equalsIgnoreCase("quit")) {
                        System.out.println("Quitting the session...");
                        sessionContinuing.cancel(true);
                        chatter.end();
                        break;
                    }
                    chatter.say(got);
                    System.out.println("Me : " + got);
                }
                pool.shutdown();
            }
        } catch (IOException io) {
            throw new RuntimeException(io);
        }

    }

}
