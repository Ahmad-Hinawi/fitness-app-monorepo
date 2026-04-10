package com.fitness.activityservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.hibernate.boot.internal.Abstract;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing

public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "fitness-activity";
    }

    @Override
    public MongoClient mongoClient() {
        // Explicitly connects to your local Mongo
        return MongoClients.create("mongodb://localhost:27017");
    }
}
