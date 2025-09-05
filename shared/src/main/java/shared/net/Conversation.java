package shared.net;

/**
 * The Conversation class represents a two-way system of communication.
 */
public class Conversation {

    private final Link l;

    public Conversation(Link l) {

        this.l = l;
        Thread t = new Thread(() -> {
            while (!l.checkHello());
        });
        //negotiate connection
        t.start();
        try {
            while (t.isAlive()) {
                Thread.sleep(500);
                l.sayHello();
            }
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    //TODO: Improve wrapper methods below

    public void say(String s) {

        l.sendMessage(s);

    }

    public String listen() {

        return l.recvMessage();

    }

    public void end() {

        l.close();

    }

}
