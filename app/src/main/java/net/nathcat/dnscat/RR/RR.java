package net.nathcat.dnscat.RR;

import net.nathcat.dnscat.DomainName;

public abstract class RR {
    public enum Class {
        IN((short) 1),
        CS((short) 3),
        HS((short) 4);

        public final short code;

        private Class(short code) { this.code = code; }

        public RR.Class fromCode(short code) {
            switch (code) {
                case 1: return IN;
                case 3: return CS;
                case 4: return HS;
            }

            throw new IllegalArgumentException("Code " + code + " not recognised as class code!");
        }
    }

    public DomainName name;
    public RR.Class cls;
    public int ttl;

    public RR(DomainName name, RR.Class cls, int ttl) {
        this.name = name;
        this.cls = cls;
        this.ttl = ttl;
    }

    abstract public byte[] rdata();
    abstract public short type();
}
