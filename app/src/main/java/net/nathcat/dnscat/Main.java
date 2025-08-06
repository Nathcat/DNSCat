package net.nathcat.dnscat;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DomainName dn = new DomainName("ddns.nathcat.net");

        FileOutputStream fos = new FileOutputStream("test.bin");
        fos.write(dn.toLabels());
        fos.flush();
        fos.close();
    }
}
