package net.nathcat.dnscat.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.nathcat.dnscat.DomainName;
import net.nathcat.dnscat.RR.RR;

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

    public Message(InputStream in) throws IOException {
        header = new Header(in);
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

    @Override
    public String toString() {
        return "----- DNS MESSAGE -----\n" 
            + header
            + "\n\n\tQuestion count: " + questions.length
            + "\n\tAnswer RR count: " + answers.length
            + "\n\tNameserver RR count: " + authorities.length
            + "\n\tAdditional RR count: " + additional.length;
    }
}
