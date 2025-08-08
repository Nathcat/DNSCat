package net.nathcat.dnscat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(53);
        byte[] buffer = new byte[1024];

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting down!");
                socket.close();
            }
        });

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            System.out.println("Got datagram from " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());
        }
    }
}
