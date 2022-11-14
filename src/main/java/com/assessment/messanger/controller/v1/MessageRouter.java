package com.assessment.messanger.controller.v1;

import com.assessment.messanger.handler.MessageHandler;
import com.assessment.messanger.utils.constants.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MessageRouter {

    @Autowired
    private MessageHandler messageHandler;

    @Bean
    public RouterFunction<ServerResponse> messageRoute() {
        return RouterFunctions.route()
                .GET(MessageConstants.ASSESMENT_V_1_MESSAGE, messageHandler::fetchMessages)
                .POST(MessageConstants.ASSESMENT_V_1_MESSAGE, messageHandler::createMessage)
                .GET(MessageConstants.ASSESMENT_V_1_MESSAGE + "/{message_id}", messageHandler::fetchMessage)
                .DELETE(MessageConstants.ASSESMENT_V_1_MESSAGE + "/{message_id}", messageHandler::deleteMessage)
                .build();
    }
}
