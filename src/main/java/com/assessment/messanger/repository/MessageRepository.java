package com.assessment.messanger.repository;

import com.assessment.messanger.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface MessageRepository {
    Mono<Message> getMessage(long messageId);

    Mono<Message> createMessage(Mono<Message> message);

    Mono<Boolean> deleteMessage(long messageId);

    Mono<Page<Message>> getMessages(Pageable pageable);

    Mono<Page<Message>> getMessagesByTopic(Pageable pageable, String topic);

    Mono<Page<Message>> getMessagesByUser(Pageable pageable, String userId);
}
