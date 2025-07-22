package client.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

public class HolePuncher {

    private static final int PORT = 23892;
    private static final int STUNPORT = 19302; //3478;
    private static final String[]  stunServers = new String[] {
            "stun.l.google.com",
            "stun.ideasip.com",
            "stun.voiparound.com",
            "stun.voipbuster.com",
            "stun.voipstunt.com",
            "stun.voxgratia.org"
    };

    private HolePuncher() {}

    public static void punch(String hostname, int amount, int interval) {

        DatagramPacket packet = null;
        DatagramSocket socket = null;
        byte[] buf = new byte[256];

        try {
            packet = new DatagramPacket(buf, 0, 0, InetAddress.getByName(hostname), PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            while (true) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void punch(InetAddress address, int amount, int interval) {

        DatagramPacket packet = null;
        DatagramSocket socket = null;
        byte[] buf = new byte[256];

        packet = new DatagramPacket(buf, 0, 0, address, PORT);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            while (true) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void punch(String hostname, int port, int amount, int interval) {

        DatagramPacket packet = null;
        DatagramSocket socket = null;
        byte[] buf = new byte[256];

        try {
            packet = new DatagramPacket(buf, 0, 0, InetAddress.getByName(hostname), port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            while (true) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static void punch(InetAddress address, int port, int amount, int interval) {

        DatagramPacket packet = null;
        DatagramSocket socket = null;
        byte[] buf = new byte[256];

        packet = new DatagramPacket(buf, 0, 0, address, PORT);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            while (true) {
                try {
                    socket.send(packet);
                    Thread.sleep(interval * 1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public static String[] getSTUN(DatagramSocket socket) {

        String[] res = new String[2];
        DatagramPacket messagePacket = null;
        DatagramPacket responsePacket = null;
        int[] tempFirstHalf = null;
        byte[] firstHalf = null;
        byte[] transID = new byte[12];
        byte[] message = null;
        byte[] response = new byte[256];
        int which = 0;

        //construct STUN request
        new SecureRandom().nextBytes(transID);
        tempFirstHalf = new int[] {
            0x00, 0x01,
            0x00, 0x00,
            0x21, 0x12, 0xA4, 0x42
        };
        firstHalf = new byte[tempFirstHalf.length];
        for (int i = 0; i < tempFirstHalf.length; i++)
            firstHalf[i] = (byte) tempFirstHalf[i];
        message = new byte[firstHalf.length + transID.length];
        int il = 0;
        for (; il < firstHalf.length; il++)
            message[il] = firstHalf[il];
        for (; il < transID.length; il++)
            message[il] = transID[il];

        //loop until we get a usable stun server, throw if none is found
        while (true) {
            try {
                messagePacket = new DatagramPacket(message, 0, message.length, InetAddress.getByName(stunServers[which]), STUNPORT);
                responsePacket = new DatagramPacket(response, 0, response.length);
                socket.send(messagePacket);
                socket.receive(responsePacket);
            } catch (IOException io) {
                if (which == stunServers.length)
                    throw new RuntimeException(io);
                else
                    ++which;
                continue;
            }
            break;
        }
        //check and format response
        res[0] = Arrays.toString(response);
        return res;

    }

}
