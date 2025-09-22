package Swiss;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import shared.SessionLocator;
import shared.net.Link;
import shared.net.Conversation;

public class Pilot {

    public static void main(String[] args) {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        ExecutorService pool;
        Conversation session;
        String got;

        System.out.println("Welcome to the Swiss P2P Messenger!");
        System.out.println("To initiate a session with an associate, please input CONNECT.");
        try {
            got = input.readLine();
            if (got.equalsIgnoreCase("connect")) {
                Link l = new Link();
                SessionLocator sus = new SessionLocator(l.querySTUNServer());
                System.out.println(sus.toString());
            }
        } catch (IOException io) {
            throw new RuntimeException(io);
        }

    }

}
