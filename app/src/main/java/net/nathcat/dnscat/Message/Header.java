package net.nathcat.dnscat.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

public class Header {
    public enum Opcode {
        QUERY((byte) 0),
        IQUERY((byte) 1),
        STATUS((byte) 2);

        public final byte code;

        private Opcode(byte code) { this.code = code; }

        public static Opcode fromCode(byte code) {
            switch (code) {
                case 0: return QUERY;
                case 1: return IQUERY;
                case 2: return STATUS;
            }

            throw new IllegalArgumentException("The code " + code + " is not recognised as an opcode!");
        }
    }

    public enum RCode {
        NOERROR((byte) 0),
        FORMATERROR((byte) 1),
        SERVERFAILURE((byte) 2),
        NAMEERROR((byte) 3),
        NOTIMPLEMENTED((byte) 4),
        REFUSED((byte) 5);

        public final byte code;

        private RCode(byte code) { this.code = code; }

        public static RCode fromCode(byte code) {
            switch (code) {
                case 0: return NOERROR;
                case 1: return FORMATERROR;
                case 2: return SERVERFAILURE;
                case 3: return NAMEERROR;
                case 4: return NOTIMPLEMENTED;
                case 5: return REFUSED;
            }

            throw new IllegalArgumentException("The code " + code + " is not recognised as a response code!");
        }
    }

    public short id;
    public boolean isResponse;
    public Opcode opcode;
    public boolean authoritative;
    public boolean truncated;
    public boolean recursionDesired;
    public boolean recursionAvailable;
    public RCode rcode;

    public Header(short id, boolean isResponse, Opcode opcode, boolean authoritative, boolean truncated, boolean recursionDesired, boolean recursionAvailable, RCode rcode) {
        this.id = id;
        this.isResponse = isResponse;
        this.opcode = opcode;
        this.authoritative = authoritative;
        this.truncated = truncated;
        this.recursionDesired = recursionDesired;
        this.recursionAvailable = recursionAvailable;
        this.rcode = rcode;
    }

    public Header(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        id = dis.readShort();
        
        byte[] B = new byte[1];
        byte b;
        dis.read(B);
        b = B[0];
        isResponse = (b >> 7) == 1;
        opcode = Opcode.fromCode((byte) ((b >> 3) & 0b111));
        authoritative = ((b >> 2) & 1) == 1;
        truncated = ((b >> 1) & 1) == 1;
        recursionDesired = (b & 1) == 1;
        
        dis.read(B);
        b = B[0];
        recursionAvailable = (b >> 7) == 1;
        rcode = RCode.fromCode((byte) (b & 0b1111));
    }
}
