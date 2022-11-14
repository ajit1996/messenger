package com.assessment.messanger.service.impl;

import com.assessment.messanger.dto.MessageDto;
import com.assessment.messanger.exception.OperationNotAllowed;
import com.assessment.messanger.exception.ValidationException;
import com.assessment.messanger.entity.Message;
import com.assessment.messanger.repository.MessageRepository;
import com.assessment.messanger.service.MessageService;
import com.assessment.messanger.utils.AppUtils;
import com.assessment.messanger.utils.constants.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Mono<MessageDto> fetchMessage(long messageId) {
        return messageRepository.getMessage(messageId).switchIfEmpty(Mono.defer(() -> Mono.error(new OperationNotAllowed(MessageConstants.MESSAGE_DOESN_T_EXISTS)))).map(AppUtils::entityToDto);
    }

    @Override
    public Mono<MessageDto> createMessage(Mono<Message> message) {
        return message.doOnNext(this::blockingValidationCall).flatMap(msg -> {
            if (!this.blockingValidationCall(msg)) {
                return messageRepository.createMessage(Mono.just(msg)).map(AppUtils::entityToDto);
            }
            return Mono.empty();
        });
    }

    private Boolean blockingValidationCall(Message message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new OperationNotAllowed(MessageConstants.TECHNICAL_ERROR);
        }
        if (Objects.isNull(message)) {
            throw new ValidationException(MessageConstants.MESSAGE_NOT_NULL_VALIDATION_MSG);
        } else if (ObjectUtils.isEmpty(message.getContent())) {
            throw new ValidationException(String.format(MessageConstants.MESSAGE_ATTRIBUTE_VALIDATION_MSG, MessageConstants.TOPIC));
        } else if (ObjectUtils.isEmpty(message.getContent())) {
            throw new ValidationException(String.format(MessageConstants.MESSAGE_ATTRIBUTE_VALIDATION_MSG, MessageConstants.CONTENT));
        }
        return false;
    }

    @Override
    public Mono<Boolean> deleteMessage(long messageId, String userId) {
        return messageRepository.getMessage(messageId).switchIfEmpty(Mono.defer(() -> Mono.error(new OperationNotAllowed(MessageConstants.MESSAGE_DOESN_T_EXISTS)))).filter(msg -> userId.equals(msg.getUserId())).switchIfEmpty(Mono.defer(() -> Mono.error(new OperationNotAllowed(MessageConstants.MESSAGE_DOESN_T_BELONG_TO_USER)))).flatMap(msg -> messageRepository.deleteMessage(messageId));
    }

    @Override
    public Mono<Map<String, Object>> fetchMessages(Pageable pageable, Optional<String> topic, Optional<String> userId) {
        if (topic.isPresent()) {
            return this.fetchMessagesByTopic(pageable, topic.get()).flatMap(this::formatPaginationResponse);
        } else if (userId.isPresent()) {
            return this.fetchMessagesByUser(pageable, userId.get()).flatMap(this::formatPaginationResponse);
        }
        return messageRepository.getMessages(pageable).flatMap(this::formatPaginationResponse);
    }

    private Mono<Map<String, Object>> formatPaginationResponse(Page<Message> messages) {
        long totalElements = messages.getTotalElements();
        int totalPages = messages.getTotalPages();
        int size = messages.getSize();
        int number = messages.getNumber();
        List<MessageDto> content = messages.get().map(AppUtils::entityToDto).collect(Collectors.toList());
        return Mono.just(Map.of("total_elements", totalElements, "total_pages", totalPages, "size", size, "page", number, "messages", content));
    }

    private Mono<Page<Message>> fetchMessagesByTopic(Pageable pageable, String topic) {
        return messageRepository.getMessagesByTopic(pageable, topic);
    }

    private Mono<Page<Message>> fetchMessagesByUser(Pageable pageable, String userId) {
        return messageRepository.getMessagesByUser(pageable, userId);
    }
}
