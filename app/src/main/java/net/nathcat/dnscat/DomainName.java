package net.nathcat.dnscat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.common.base.Charsets;

import net.nathcat.dnscat.Message.Message;

/**
 * Encapsulates a domain name as a readable string.
 */
public class DomainName {
    /**
     * Quick reference for the origin domain name "@"
     */
    public static final DomainName origin = new DomainName("@");

    /**
     * The readable domain name
     */
    private String name;

    public DomainName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    /**
     * Convert this domain name into a series of labels, as described in <a href="https://datatracker.ietf.org/doc/html/rfc1035#section-4.1.4">RFC 1035</a>
     * @return A byte array containing the label data.
     */
    public byte[] toLabels(HashMap<String, Short> compressionTable, short offset) {
        if (name.equals("@")) {
            return new byte[] {0};
        }

        if (compressionTable != null && compressionTable.containsKey(name)) {
            return new DomainNamePointer(compressionTable.get(name)).toLabels(compressionTable, offset);
        }
        else if (compressionTable != null) {
            compressionTable.put(name, offset);
        }

        String[] labels = name.split("\\.");
        ArrayList<Byte> list = new ArrayList<>();
        
        for (String label : labels) {
            list.add((byte) label.length());
            
            for (char c : label.toCharArray()) {
                list.add((byte) c);
            }
        }

        Byte[] ba = list.toArray(new Byte[0]);
        byte[] ta = new byte[ba.length + 1];
        for (int i = 0; i < ba.length; i++) ta[i] = ba[i];

        ta[ta.length - 1] = 0;

        return ta;
    }

    public static DomainName fromLabels(InputStream in) throws IOException {
        byte[] n = new byte[1];
        in.read(n);

        if (n[0] == 0) return DomainName.origin;

        if ((n[0] & 0xC000) == 0xC000) {
            return new DomainNamePointer((short) (n[0] & 0x3FFF));
        }

        StringBuilder name = new StringBuilder();
        while (n[0] != 0) {
            byte[] c = new byte[n[0]];
            in.read(c);
            name.append(new String(c, Charsets.US_ASCII));

            in.read(n);

            if (n[0] != 0) name.append(".");
        }

        return new DomainName(name.toString());
    }
}
