package be.fastrada.service;

import be.fastrada.model.Race;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RaceService {

    @Autowired
    private MongoTemplate mongoTemplate;
    private static final String COLLECTION_NAME = "races";

    public List getAllRaces() {
        return mongoTemplate.findAll(Race.class);
    }

    public Race getRaceById(String raceId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(raceId)));
        return mongoTemplate.findOne(query, Race.class, COLLECTION_NAME);
    }

    public void addRace(String raceName, DateTime startTime) {
        mongoTemplate.insert(new Race(raceName, startTime));
    }

    public Race getRaceByNameAndTime(String raceName, DateTime startTime) {
        Query q = new Query(Criteria.where("name").is(raceName).and("dateTime").is(startTime));
        return mongoTemplate.findOne(q, Race.class);
    }
}
