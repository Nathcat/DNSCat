package net.nathcat.dnscat.RR;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.nathcat.dnscat.DomainName;

public class A extends RR {
    public InetAddress address;

    public A(DomainName name, RR.Class cls, int ttl, InetAddress address) {
        super(name, cls, ttl);
        this.address = address;
    }

    public A(DomainName name, RR.Class cls, int ttl, InputStream rdata) throws UnknownHostException, IOException {
        super(name, cls, ttl);
        
        DataInputStream dis = new DataInputStream(rdata);
        short rdlength = dis.readShort();
        byte[] addr = new byte[rdlength];
        rdata.read(addr);
        address = InetAddress.getByAddress(addr);
    }

    @Override
    public byte[] rdata() {
        return address.getAddress();
    }

    @Override
    public short type() {
        return (short) 1;
    }

    @Override
    public byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.write(super.getBytes());
            dos.writeShort(address.getAddress().length);
            dos.write(address.getAddress());
            dos.flush();

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
