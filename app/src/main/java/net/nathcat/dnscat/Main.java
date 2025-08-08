package net.nathcat.dnscat;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import net.nathcat.dnscat.Message.Header;
import net.nathcat.dnscat.Message.Message;

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
            socket.receive(packet);

            byte[] data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
            System.out.println("Got datagram from " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());

            FileOutputStream fos = new FileOutputStream("test.bin");
            fos.write(data);
            fos.close();
            
            Message msg = new Message(new ByteArrayInputStream(data));
            System.out.println(msg);
        }
    }
}
