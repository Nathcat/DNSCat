package net.nathcat.dnscat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import net.nathcat.dnscat.Message.Header;
import net.nathcat.dnscat.Message.Message;
import net.nathcat.dnscat.Message.Question;
import net.nathcat.dnscat.RR.RR;
import net.nathcat.dnscat.exceptions.InvalidCodeException;

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
            
            Message msg;
            try {
                msg = new Message(new ByteArrayInputStream(data));
                
                byte[] reply = new Message(new Header(
                    msg.header.id, true, Header.Opcode.QUERY, true, false, false, false, Header.RCode.NAMEERROR
                ), new Question[0], new RR[0], new RR[0], new RR[0]).getBytes();

                DatagramPacket replyPacket = new DatagramPacket(reply, 0, reply.length, packet.getAddress(), packet.getPort());
                replyPacket.setAddress(packet.getAddress());
                replyPacket.setPort(packet.getPort());
                socket.send(replyPacket);
                
            } catch (IOException e) {
                e.printStackTrace();

            } catch (InvalidCodeException e) {
                byte[] reply = new Message(new Header(
                    (short) Math.random(), true, Header.Opcode.QUERY, true, false, false, false, Header.RCode.NOTIMPLEMENTED
                ), new Question[0], new RR[0], new RR[0], new RR[0]).getBytes();

                DatagramPacket replyPacket = new DatagramPacket(reply, 0, reply.length, packet.getAddress(), packet.getPort());
                replyPacket.setAddress(packet.getAddress());
                replyPacket.setPort(packet.getPort());
                socket.send(replyPacket);
            }
        }
    }
}
