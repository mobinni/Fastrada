package be.fastrada.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = "sensors")
public class Sensor {

    @Id
    private String id;

    private String name;
    private String[] types;

    public Sensor(String name, String[] types) {
        this.name = name;
        this.types = types;
    }

    public String[] getTypes() {
        return types;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
