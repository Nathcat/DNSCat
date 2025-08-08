package net.nathcat.dnscat.exceptions;

public class InvalidCodeException extends Exception {
    private final String msg;
    public short id;

    public InvalidCodeException(String msg) { this.msg = msg; }

    @Override
    public String toString() {
        return "[Transaction ID: " + id + "]" + msg;
    }
}
