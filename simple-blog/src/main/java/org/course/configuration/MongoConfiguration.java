package org.course.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

//@Configuration
public class MongoConfiguration {

    @Bean
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDatabaseFactory){
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

}
