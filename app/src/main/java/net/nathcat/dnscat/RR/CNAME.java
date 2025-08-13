package net.nathcat.dnscat.RR;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import net.nathcat.dnscat.DomainName;
import net.nathcat.dnscat.DomainNamePointer;

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
        return alias.toLabels(null, (short) 0);
    }

    @Override
    public short type() {
        return (short) 5;
    }

    @Override
    public String toString() {
        return "CNAME -- " + name.name() + " -- " + cls + " -- " + ttl + " -- " + alias.name();  
    }

    @Override
    public void write(ByteArrayOutputStream baos, HashMap<String, Short> compressionTable) throws IOException {
        super.write(baos, compressionTable);
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] n = alias.toLabels(compressionTable, (short) baos.size());
        dos.writeShort(n.length);
        dos.flush();
        baos.write(n);
    }
    
}
