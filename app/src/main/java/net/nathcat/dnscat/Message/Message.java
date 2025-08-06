package net.nathcat.dnscat.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

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
        short additional = dis.readShort();  // Additional count

        questions = Question.readQuestions(qd, dis);
        // TODO Read RR records
    }
}
