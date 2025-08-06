package net.nathcat.dnscat.RR;

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
        byte[] addr = new byte[4];
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
    
}
