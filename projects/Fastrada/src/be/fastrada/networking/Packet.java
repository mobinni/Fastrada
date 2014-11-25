package be.fastrada.networking;

import org.joda.time.DateTime;

public class Packet {
    private byte[] content;
    private DateTime timestamp;

    public Packet(byte[] content, DateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public byte[] getContent() {
        return content;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
