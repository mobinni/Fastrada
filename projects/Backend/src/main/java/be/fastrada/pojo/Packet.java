package be.fastrada.pojo;

import org.joda.time.DateTime;

public class Packet {
    private byte[] content;
    private DateTime timestamp;

    public Packet() {

    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }
}