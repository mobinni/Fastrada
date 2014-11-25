package be.fastrada;

import be.fastrada.config.SpringMongoConfig;
import be.fastrada.model.Packet;
import be.fastrada.model.Race;
import be.fastrada.model.Sensor;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static junit.framework.TestCase.assertTrue;

public class MongoTests {

    @Test
    public void addRace() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        final String COLLECTION_NAME = "packets";

        // Gen race
        Race race = new Race("TestRace", new DateTime());

        mongoOperation.save(race);

        // Gen sensors
        Sensor engine = new Sensor("Engine", new String[]{"rpm", "temp"});
        Sensor carData = new Sensor("CarData", new String[]{"speed", "gear"});

        mongoOperation.save(engine);
        mongoOperation.save(carData);

        // Gen packets
        Packet packet1 = new Packet(5000, new DateTime(), race.getId(), "rpm");
        Packet packet2 = new Packet(80, new DateTime(), race.getId(), "temp");
        Packet packet3 = new Packet(100, new DateTime(), race.getId(), "speed");
        Packet packet4 = new Packet(5, new DateTime(), race.getId(), "gear");

        mongoOperation.save(packet1, COLLECTION_NAME);
        mongoOperation.save(packet2, COLLECTION_NAME);
        mongoOperation.save(packet3, COLLECTION_NAME);
        mongoOperation.save(packet4, COLLECTION_NAME);

        // gen testdata
        Packet[] packets = new Packet[10000];
        for (int i = 0; i < 10000; i += 4) {
            packets[i] = new Packet((Math.random() * 5000 + 1), new DateTime(), race.getId(), "rpm");
            packets[i + 1] = new Packet((Math.random() * 90 + 1), new DateTime(), race.getId(), "temp");
            packets[i + 2] = new Packet((Math.random() * 160 + 1), new DateTime(), race.getId(), "speed");
            packets[i + 3] = new Packet((Math.random() * 6 + 1), new DateTime(), race.getId(), "gear");
        }

        for (Packet p : packets) {
            mongoOperation.insert(p, COLLECTION_NAME);
        }

        // mongo operations examples
        // query db for the race
        Query query = new Query(Criteria.where("name").is("TestRace"));

        // find the race with the query
        Race foundRace = mongoOperation.findOne(query, Race.class);
        System.out.println("Found race: " + foundRace);

        // delete race, not enabled to make sure we have data in the db
        // mongoOperation.remove(query, Race.class);

        // list races, not enabled to prevent spam
        // List<Race> raceList = mongoOperation.findAll(Race.class);
        // System.out.println("number of races: " + raceList.size());

    }

    @Test
    public void checkPacketString()
    {
        Packet p = new Packet(0, null, "racenaam", "type");

        assertTrue(p.toString().contains("racenaam"));
    }

    @Test
    public void checkSensorString()
    {
        String[] strings = new String[1];
        strings[0] = "hallo";
        Sensor s = new Sensor("cardata", strings);

        assertTrue(s.toString().contains("hallo"));
        assertTrue(s.toString().contains("cardata"));
    }
}
