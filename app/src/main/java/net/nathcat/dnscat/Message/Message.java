package net.nathcat.dnscat.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.nathcat.dnscat.DomainName;
import net.nathcat.dnscat.RR.RR;
import net.nathcat.dnscat.exceptions.InvalidCodeException;

public class Message {
    public Header header;
    public Question[] questions;
    public RR[] answers;
    public RR[] authorities;
    public RR[] additional;

    public Message(Header header, Question[] questions, RR[] answers, RR[] authorities, RR[] additional) {
        this.header = header;
        this.questions = questions;
        this.answers = answers;
        this.authorities = authorities;
        this.additional = additional;
    }

    public Message(InputStream in) throws IOException, InvalidCodeException {
        header = new Header(in);
        
        try {
            DataInputStream dis = new DataInputStream(in);
            short qd = dis.readShort();  // Question count
            short an = dis.readShort();  // Answer count
            short ns = dis.readShort();  // Authority count
            short ad = dis.readShort();  // Additional count

            questions = Question.readQuestions(qd, dis);
            
            answers = new RR[an];
            for (int i = 0; i < an; i++) {
                answers[i] = new RR.Builder()
                    .domainName(DomainName.fromLabels(dis))
                    .type(dis.readShort())
                    .cls(dis.readShort())
                    .ttl(dis.readInt())
                    .build(dis);
            }

            authorities = new RR[ns];
            for (int i = 0; i < ns; i++) {
                authorities[i] = new RR.Builder()
                    .domainName(DomainName.fromLabels(dis))
                    .type(dis.readShort())
                    .cls(dis.readShort())
                    .ttl(dis.readInt())
                    .build(dis);
            }

            additional = new RR[ad];
            for (int i = 0; i < ad; i++) {
                additional[i] = new RR.Builder()
                    .domainName(DomainName.fromLabels(dis))
                    .type(dis.readShort())
                    .cls(dis.readShort())
                    .ttl(dis.readInt())
                    .build(dis);
            }
        }
        catch (InvalidCodeException e) {
            e.id = header.id;
            throw e;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("----- DNS MESSAGE -----\n"
                + header
                + "\n\n\tQuestion count: " + questions.length
                + "\n\tAnswer RR count: " + answers.length
                + "\n\tNameserver RR count: " + authorities.length
                + "\n\tAdditional RR count: " + additional.length);

        s.append("----- Questions\n");
        for (Question q : questions) { s.append(q.toString() + "\n"); }
        s.append("----- Answers\n");
        for (RR r : answers) { s.append(r.toString() + "\n"); }
        s.append("----- Authoritative name servers\n");
        for (RR r : authorities) { s.append(r.toString() + "\n"); }
        s.append("----- Additional records\n");
        for (RR r : additional) { s.append(r.toString() + "\n"); }
        
        return s.toString();
    }

    public byte[] getBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.write(header.getBytes());
            dos.writeShort(questions.length);
            dos.writeShort(answers.length);
            dos.writeShort(authorities.length);
            dos.writeShort(additional.length);

            for (Question q : questions) {
                dos.write(q.getBytes());
            }

            for (RR r : answers) {
                dos.write(r.getBytes());
            }

            for (RR r : authorities) {
                dos.write(r.getBytes());
            }

            for (RR r : additional) {
                dos.write(r.getBytes());
            }

            dos.flush();

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
