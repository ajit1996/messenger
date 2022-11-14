package com.assessment.messanger.controller.v2;

import com.assessment.messanger.dto.MessageDto;
import com.assessment.messanger.entity.Message;
import com.assessment.messanger.service.MessageService;
import com.assessment.messanger.utils.constants.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
    @RequestMapping(MessageConstants.ASSESMENT_V_2_MESSAGE)
public class MessageResource {

    @Autowired
    private MessageService messageService;

    @GetMapping(path = "{message_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MessageDto> getMessage(@PathVariable(value = MessageConstants.MESSAGE_ID) long messageId) {
        return messageService.fetchMessage(messageId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getAllMessage(@RequestParam(value = MessageConstants.PAGE, required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = MessageConstants.SIZE, required = false, defaultValue = "10") int size,
                                                  @RequestParam(value = MessageConstants.TOPIC, required = false) Optional<String> topic,
                                                  @RequestParam(value = MessageConstants.USER_ID, required = false) Optional<String> userId) {
        return messageService.fetchMessages(PageRequest.of(page, size), topic, userId);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MessageDto> createMessage(@RequestBody Message message, Principal principal) {
        message.setUserId(principal.getName());
        return messageService.createMessage(Mono.just(message));
    }

    @DeleteMapping(path = "{message_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Boolean> deleteMessage(@PathVariable(value = MessageConstants.MESSAGE_ID) long messageId, Principal principal) {
        String userId = principal.getName();
        return messageService.deleteMessage(messageId, userId);
    }
}
