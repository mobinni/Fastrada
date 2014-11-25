package be.fastrada.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "races")
public class Race {

    @Id
    private String id;

    private String name;
    private DateTime dateTime;

    public Race(String name, DateTime dateTime) {
        this.name = name;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Race{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}