package net.nathcat.dnscat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.base.Charsets;

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
    public String name;

    public DomainName(String name) {
        this.name = name;
    }

    /**
     * Convert this domain name into a series of labels, as described in <a href="https://datatracker.ietf.org/doc/html/rfc1035#section-4.1.4">RFC 1035</a>
     * @return A byte array containing the label data.
     */
    public byte[] toLabels() {
        if (name.equals("@")) {
            return new byte[] {0};
        }

        String[] labels = name.split("\\.");
        ArrayList<Byte> list = new ArrayList<>();
        
        for (String label : labels) {
            System.out.println("First label is " + label);
            list.add((byte) label.length());
            
            for (char c : label.toCharArray()) {
                list.add((byte) c);
            }
        }

        Byte[] ba = list.toArray(new Byte[0]);
        byte[] ta = new byte[ba.length + 1];
        for (int i = 0; i < ba.length; i++) ta[i] = ba[i];

        ta[ta.length - 1] = 0;

        System.out.println(Arrays.toString(ta));
        return ta;
    }

    public static DomainName fromLabels(InputStream in) throws IOException {
        byte[] n = new byte[1];
        in.read(n);

        if (n[0] == 0) return DomainName.origin;

        StringBuilder name = new StringBuilder();
        while (n[0] != 0) {
            byte[] c = new byte[n[0]];
            in.read(c);
            name.append(new String(c, Charsets.US_ASCII));

            in.read(n);
        }

        return new DomainName(in.toString());
    }
}
