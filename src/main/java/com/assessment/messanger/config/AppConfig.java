package com.assessment.messanger.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
public class AppConfig {

    public static final String DATABASE_NAME = "messenger_db";

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    }

    @Bean
    public ObjectWriter objectWriter(ObjectMapper objectMapper) {
        return objectMapper.writerWithDefaultPrettyPrinter();
    }

    @Bean
    public ReactiveMongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleReactiveMongoDatabaseFactory(mongoClient, DATABASE_NAME);
    }

    @Bean(name = "reactiveMongoTemplate")
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory mongoDatabaseFactory) {
        return new ReactiveMongoTemplate(mongoDatabaseFactory);
    }

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
