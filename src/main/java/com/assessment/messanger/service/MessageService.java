package com.assessment.messanger.service;

import com.assessment.messanger.dto.MessageDto;
import com.assessment.messanger.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

public interface MessageService {

    Mono<MessageDto> fetchMessage(long messageId);

    Mono<MessageDto> createMessage(Mono<Message> message);

    Mono<Boolean> deleteMessage(long messageId, String userId);

    Mono<Map<String, Object>> fetchMessages(Pageable pageable, Optional<String> topic, Optional<String> userId);
}
