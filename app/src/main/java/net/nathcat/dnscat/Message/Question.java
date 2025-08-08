package net.nathcat.dnscat.Message;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;

import net.nathcat.dnscat.DomainName;
import net.nathcat.dnscat.exceptions.InvalidCodeException;

public class Question {
    public enum QType {
        A((short) 1),
        CNAME((short) 5),
        MX((short) 15),
        TXT((short) 16),
        AXFR((short) 252),
        MAILB((short) 253),
        ALL((short) 255);

        public final short code;

        private QType(short code) { this.code = code; }

        public static QType fromCode(short code) throws InvalidCodeException {
            switch (code) {
                case 1: return A;
                case 5: return CNAME;
                case 15: return MX;
                case 16: return TXT;
                case 252: return AXFR;
                case 253: return MAILB;
                case 255: return ALL;
            }

            throw new InvalidCodeException("Code " + code + " not recognised as type code!");
        }
    }

    public enum QClass {
        IN((short) 1),
        CS((short) 3),
        HS((short) 4),
        ALL((short) 255);

        public final short code;

        private QClass(short code) { this.code = code; }

        public static QClass fromCode(short code) {
            switch (code) {
                case 1: return IN;
                case 3: return CS;
                case 4: return HS;
                case 255: return ALL;
            }

            throw new IllegalArgumentException("Code " + code + " not recognised as class code!");
        }
    }

    public DomainName name;
    public QType type;
    public QClass cls;

    public Question(DomainName name, QType type, QClass cls) {
        this.name = name;
        this.type = type;
        this.cls = cls;
    }

    public Question(InputStream in) throws IOException, InvalidCodeException {
        this.name = DomainName.fromLabels(in);
        
        DataInputStream dis = new DataInputStream(in);
        type = QType.fromCode(dis.readShort());
        cls = QClass.fromCode(dis.readShort());
    }

    public byte[] getBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(name.toLabels());
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(type.code);
            dos.writeShort(cls.code);
            dos.flush();

            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Question[] readQuestions(int questionCount, InputStream in) throws IOException, InvalidCodeException {
        Question[] q = new Question[questionCount];

        for (int i = 0; i < questionCount; i++) q[i] = new Question(in);

        return q;
    }

    @Override
    public String toString() {
        return "Question -- " + name.name + " -- " + type + " -- " + cls;
    }
}
