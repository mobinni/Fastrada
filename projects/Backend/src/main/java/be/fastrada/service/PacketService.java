package be.fastrada.service;

import be.fastrada.model.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PacketService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "packets";

    public void addPacket(Packet packet) {
        // If the collection does not exist, create it
        if (!mongoTemplate.collectionExists(Packet.class)) {
            mongoTemplate.createCollection(Packet.class);
        }

        // Create a new packet
        mongoTemplate.insert(packet, COLLECTION_NAME);
    }

    public List getPacketsByRaceId(String raceId, String filterKey) {
        Query queryData;

        if (filterKey.equals("") || filterKey == null) {
            queryData = new Query(Criteria.where("raceId").is(raceId));
        } else {
            queryData = new Query(Criteria.where("raceId").is(raceId).and("type").is(filterKey));
        }

        return mongoTemplate.find(queryData, Packet.class, COLLECTION_NAME);
    }
}
