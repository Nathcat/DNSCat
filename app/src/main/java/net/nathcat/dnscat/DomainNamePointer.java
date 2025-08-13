package net.nathcat.dnscat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.nathcat.dnscat.Message.Message;

public class DomainNamePointer extends DomainName {
    private final int offset;

    public DomainNamePointer(short offset) {
        super(null);
        this.offset = offset;
    }
    
    public DomainName resolve(Message msg) throws IOException {
        ByteArrayInputStream buffer = new ByteArrayInputStream(msg.getBytes());
        buffer.skip(offset);
        return DomainName.fromLabels(buffer);
    }

    @Override
    public String name() {
        return "<unresolved domain name pointer>";
    }

    @Override
    public byte[] toLabels(HashMap<String, Short> compressionTable, short offset) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeShort(0xC000 | offset);
            dos.flush();

            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
