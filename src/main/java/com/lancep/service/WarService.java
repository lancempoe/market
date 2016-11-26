package com.lancep.service;


import com.lancep.config.MongoDBConfig;
import com.lancep.war.errorhandling.WarException;
import com.lancep.war.factory.WarFactory;
import com.lancep.war.orm.War;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Component
public class WarService {

    private static final Logger logger = Logger.getLogger( WarService.class.getName() );

    @Autowired
    private MongoDBConfig mongoConfig;

    public List<War> getWarGames() {
        try {
            MongoOperations mongoOperation = mongoConfig.mongoTemplate();
            return mongoOperation.findAll(War.class);
        } catch (Exception e) {
            throw new WarException(Response.Status.GATEWAY_TIMEOUT);
        }
    }

    public String createWarGame(int numberOfSuits, int numberOfRank) {

        try {
            MongoOperations mongoOperation = mongoConfig.mongoTemplate();

            War war = WarFactory.createWar(numberOfSuits, numberOfRank);
            mongoOperation.save(war);

            logger.info(String.format("New War Created: %s", war.getId()));
            return war.getId();

        } catch (Exception e) {
            throw new WarException(Response.Status.GATEWAY_TIMEOUT);
        }
    }

    public War getWarGame(String id) {
        War war;
        try {
            MongoOperations mongoOperation = mongoConfig.mongoTemplate();
            war = mongoOperation.findById(id, War.class);
        } catch (Exception e) {
            throw new WarException(Response.Status.GATEWAY_TIMEOUT);
        }
        if (war == null) {
            throw new WarException(Response.Status.BAD_REQUEST);
        }
        return war;
    }

    public void deleteWarGame(String id) {
        War war;
        MongoOperations mongoOperation;
        try {
            mongoOperation = mongoConfig.mongoTemplate();
            war = mongoOperation.findById(id, War.class);
        } catch (Exception e) {
            throw new WarException(Response.Status.GATEWAY_TIMEOUT);
        }
        if (war == null) {
            throw new WarException(Response.Status.BAD_REQUEST);
        }
        mongoOperation.remove(war);
    }
}
