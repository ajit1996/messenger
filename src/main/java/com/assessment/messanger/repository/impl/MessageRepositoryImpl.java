package com.assessment.messanger.repository.impl;

import com.assessment.messanger.entity.Message;
import com.assessment.messanger.repository.MessageRepository;
import com.assessment.messanger.utils.constants.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Message> getMessage(long messageId) {
        return reactiveMongoTemplate.findById(messageId, Message.class);
    }

    @Override
    public Mono<Message> createMessage(Mono<Message> message) {
        return reactiveMongoTemplate.save(message);
    }

    @Override
    public Mono<Boolean> deleteMessage(long messageId) {
        Query fetchMessage = Query.query(Criteria.where(MessageConstants.MESSAGE_ID).is(messageId));
        return reactiveMongoTemplate.remove(fetchMessage, Message.class)
                .flatMap(deleteResult -> Mono.just(deleteResult.wasAcknowledged()));
    }

    @Override
    public Mono<Page<Message>> getMessages(Pageable pageable) {
        Query fetchMessagesPaginationQuery = new Query().with(pageable);
        Flux<Message> messageFlux = reactiveMongoTemplate.find(fetchMessagesPaginationQuery, Message.class);
        return Mono.zip(messageFlux.collectList(), messageFlux.count()).map(tuple2 -> PageableExecutionUtils.getPage(tuple2.getT1(), pageable, () -> tuple2.getT2()));
    }

    @Override
    public Mono<Page<Message>> getMessagesByTopic(Pageable pageable, String topic) {
        Query fetchMessagesPaginationQuery = new Query(Criteria.where("topic").is(topic)).with(pageable);
        Flux<Message> messageFlux = reactiveMongoTemplate.find(fetchMessagesPaginationQuery, Message.class);
        return Mono.zip(messageFlux.collectList(), messageFlux.count()).map(tuple2 -> PageableExecutionUtils.getPage(tuple2.getT1(), pageable, () -> tuple2.getT2()));
    }

    @Override
    public Mono<Page<Message>> getMessagesByUser(Pageable pageable, String userId) {
        Query fetchMessagesPaginationQuery = new Query(Criteria.where("user_id").is(userId)).with(pageable);
        Flux<Message> messageFlux = reactiveMongoTemplate.find(fetchMessagesPaginationQuery, Message.class);
        return Mono.zip(messageFlux.collectList(), messageFlux.count()).map(tuple2 -> PageableExecutionUtils.getPage(tuple2.getT1(), pageable, () -> tuple2.getT2()));
    }
}
