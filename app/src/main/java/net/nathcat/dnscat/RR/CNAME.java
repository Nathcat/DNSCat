package net.nathcat.dnscat.RR;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.nathcat.dnscat.DomainName;

public class CNAME extends RR {
    public DomainName alias;

    public CNAME(DomainName name, RR.Class cls, int ttl, DomainName alias) {
        super(name, cls, ttl);
        this.alias = alias;
    }

    public CNAME(DomainName name, RR.Class cls, int ttl, InputStream rdata) throws IOException {
        super(name, cls, ttl);
        byte[] rdlength = new byte[2];
        rdata.read(rdlength); 
        alias = DomainName.fromLabels(rdata);
    }

    @Override
    public byte[] rdata() {
        return alias.toLabels();
    }

    @Override
    public short type() {
        return (short) 5;
    }

    @Override
    public byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.write(super.getBytes());
            byte[] n = alias.toLabels();
            dos.writeShort(n.length);
            dos.write(n);
            dos.flush();

            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
