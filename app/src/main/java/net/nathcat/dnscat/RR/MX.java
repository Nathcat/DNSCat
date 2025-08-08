package net.nathcat.dnscat.RR;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.nathcat.dnscat.DomainName;

public class MX extends RR {
    public short preference;
    public DomainName exchange;

    public MX(DomainName name, RR.Class cls, int ttl, short preference, DomainName exchange) {
        super(name, cls, ttl);
        this.preference = preference;
        this.exchange = exchange;
    }

    public MX(DomainName name, RR.Class cls, int ttl, InputStream rdata) {
        super(name, cls, ttl);
        DataInputStream dis = new DataInputStream(rdata);
        try {
            byte[] rdlength = new byte[2];
            dis.read(rdlength); 
            preference = dis.readShort();
            exchange = DomainName.fromLabels(dis);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] rdata() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeShort(preference);
            dos.write(exchange.toLabels());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return baos.toByteArray();
    }

    @Override
    public short type() {
        return (short) 15;
    }
    
}
