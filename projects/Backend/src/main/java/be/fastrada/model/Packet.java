package be.fastrada.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "packets")
public class Packet {

    @Id
    private String id;

    private double content;
    private DateTime timestamp;
    private String raceId;
    private String type;

    public Packet(double content, DateTime timestamp, String raceId, String type) {
        this.content = content;
        this.timestamp = timestamp;
        this.raceId = raceId;
        this.type = type;
    }

    public double getContent() {
        return content;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public String getRaceId() {
        return raceId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", time=" + timestamp +
                ", raceId='" + raceId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}