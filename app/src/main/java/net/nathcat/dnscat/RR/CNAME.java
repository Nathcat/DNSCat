package net.nathcat.dnscat.RR;

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
    
}
