package net.nathcat.dnscat.RR;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import net.nathcat.dnscat.DomainName;

public abstract class RR {

    public static class Builder {
        private DomainName name;
        private RR.Class cls;
        private int ttl;
        private java.lang.Class<? extends RR> type;

        public Builder domainName(DomainName name) { this.name = name;  return this; }
        public Builder cls(RR.Class cls) { this.cls = cls;  return this; }
        public Builder cls(short code) { cls(Class.fromCode(code));  return this; }
        public Builder ttl(int ttl) { this.ttl = ttl;  return this; }
        public Builder type(java.lang.Class<? extends RR> type) { this.type = type;  return this; }

        public Builder type(Type type) {
            switch (type) {
                case A: this.type = A.class; return this;
                case CNAME: this.type = CNAME.class; return this;
                case MX: this.type = MX.class; return this;
                case TXT: this.type = TXT.class; return this;
            }

            throw new IllegalArgumentException("Provided type not implemented!");
        }

        public Builder type(short code) {
            type(Type.fromCode(code));
            return this;
        }

        public RR build(InputStream rdata) {
            try {
                return type.getConstructor(DomainName.class, RR.Class.class, int.class, InputStream.class)
                    .newInstance(name, cls, ttl, rdata);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public enum Class {
        IN((short) 1),
        CS((short) 3),
        HS((short) 4);

        public final short code;

        private Class(short code) { this.code = code; }

        public static RR.Class fromCode(short code) {
            switch (code) {
                case 1: return IN;
                case 3: return CS;
                case 4: return HS;
            }

            throw new IllegalArgumentException("Code " + code + " not recognised as class code!");
        }
    }

    public enum Type {
        A((short) 1),
        CNAME((short) 5),
        MX((short) 15),
        TXT((short) 16);

        public final short code;

        private Type(short code) { this.code = code; }

        public static RR.Type fromCode(short code) {
            switch (code) {
                case 1: return A;
                case 5: return CNAME;
                case 15: return MX;
                case 16: return TXT;
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
