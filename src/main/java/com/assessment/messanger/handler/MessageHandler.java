package com.assessment.messanger.handler;

import com.assessment.messanger.dto.MessageDto;
import com.assessment.messanger.entity.Message;
import com.assessment.messanger.exception.ValidationException;
import com.assessment.messanger.service.MessageService;
import com.assessment.messanger.utils.constants.MessageConstants;
import com.assessment.messanger.validator.AbstractValidationHandler;
import com.assessment.messanger.validator.MessageRequestEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessageHandler extends AbstractValidationHandler<Message, MessageRequestEntityValidator> {

    @Autowired
    private MessageService messageService;

    public Mono<ServerResponse> fetchMessages(ServerRequest serverRequest) {
        Optional<String> optional = serverRequest.queryParam(MessageConstants.PAGE);
        int page = getRequestParamIntValue(optional, 0);
        optional = serverRequest.queryParam(MessageConstants.SIZE);
        int size = getRequestParamIntValue(optional, 10);

        Optional<String> topic = serverRequest.queryParam(MessageConstants.TOPIC);
        Optional<String> userId = serverRequest.queryParam(MessageConstants.USER_ID);
        Mono<Map<String, Object>> messages = messageService.fetchMessages(PageRequest.of(page, size), topic, userId);
        return ServerResponse.ok().body(messages, Map.class);
    }

    private static int getRequestParamIntValue(Optional<String> optional, int attribute) {
        try {
            if (optional.isPresent()) {
                return Integer.parseInt(optional.get());
            } else {
                return attribute;
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Type mismatch.");
        }
    }

    public Mono<ServerResponse> fetchMessage(ServerRequest serverRequest) {
        long messageId = Long.parseLong(serverRequest.pathVariable(MessageConstants.MESSAGE_ID));
        Mono<MessageDto> messages = messageService.fetchMessage(messageId);
        return ServerResponse.ok().body(messages, Message.class);
    }

    public Mono<ServerResponse> createMessage(ServerRequest serverRequest) {
        Mono<String> userIdMono = serverRequest.principal().map(Principal::getName);
        Mono<Message> message = userIdMono.flatMap(userId ->
                serverRequest.bodyToMono(Message.class)
                .doOnNext(msg -> msg.setUserId(userId))
                        .doOnNext(msg -> {
                            if (Objects.isNull(msg.getCreatedOn())) msg.setCreatedOn(LocalDateTime.now());
                        }));
        return ServerResponse.ok().body(messageService.createMessage(message), MessageDto.class);
    }

    public Mono<ServerResponse> deleteMessage(ServerRequest serverRequest) {
        Mono<String> userIdMono = serverRequest.principal().map(Principal::getName);
        long messageId = Long.parseLong(serverRequest.pathVariable(MessageConstants.MESSAGE_ID));
        Mono<Map<String, Object>> booleanMono = userIdMono.flatMap(userId -> messageService.deleteMessage(messageId, userId)).flatMap(MessageHandler::booleanToStatus);
        return ServerResponse.ok().body(booleanMono, Boolean.class);
    }

    private static Mono<Map<String, Object>> booleanToStatus(Boolean aBoolean) {
        Map<String, Object> deleteResponseEntity = new HashMap<>();
        if (aBoolean) {
            deleteResponseEntity.put(MessageConstants.MESSAGE, MessageConstants.MESSAGE_DELETE_SUCCESS_MSG);
        } else {
            deleteResponseEntity.put(MessageConstants.MESSAGE, MessageConstants.MESSAGE_DELETE_FAILURE_MSG);
        }
        return Mono.just(deleteResponseEntity);
    }

    private MessageHandler() {
        super(Message.class, new MessageRequestEntityValidator());
    }

    @Override
    protected Mono<ServerResponse> processBody(Message message, ServerRequest originalRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(this.createMessage(originalRequest), Message.class);
    }

}
