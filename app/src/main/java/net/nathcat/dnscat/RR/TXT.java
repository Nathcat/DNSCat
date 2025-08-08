package net.nathcat.dnscat.RR;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.google.common.base.Charsets;

import net.nathcat.dnscat.DomainName;

public class TXT extends RR {
    public String data;

    public TXT(DomainName name, RR.Class cls, int ttl, String data) {
        super(name, cls, ttl);
        this.data = data;
    }

    public TXT(DomainName name, RR.Class cls, int ttl, InputStream rdata) throws IOException {
        super(name, cls, ttl);
        byte[] rdlength = new byte[2];
        rdata.read(rdlength); 
        try (Scanner s = new Scanner(rdata)) {
            this.data = new String(s.nextLine().getBytes(Charsets.US_ASCII), Charsets.US_ASCII);  // Help
        }
    }

    @Override
    public byte[] rdata() {
        return data.getBytes(Charsets.US_ASCII);
    }

    @Override
    public short type() {
        return (short) 16;
    }
    
    @Override
    public byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.write(super.getBytes());
            byte[] n = data.getBytes(Charsets.US_ASCII);
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
