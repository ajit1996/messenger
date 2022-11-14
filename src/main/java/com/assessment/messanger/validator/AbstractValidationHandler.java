package com.assessment.messanger.validator;

import com.assessment.messanger.utils.MessageUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractValidationHandler<T, U extends Validator> {

    private final Class<T> validationClass;

    private final U validator;

    protected AbstractValidationHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    abstract protected Mono<ServerResponse> processBody(T validBody, final ServerRequest originalRequest);

    public final Mono<ServerResponse> handleRequest(final ServerRequest request) {
        return request.bodyToMono(this.validationClass).flatMap(body -> {
            Errors errors = new BeanPropertyBindingResult(body, this.validationClass.getName());
            this.validator.validate(body, errors);

            if (errors == null || errors.getAllErrors().isEmpty()) {
                return processBody(body, request);
            } else {
                return onValidationErrors(errors, body, request);
            }
        });
    }

    protected Mono<ServerResponse> onValidationErrors(Errors errors, T invalidBody, final ServerRequest request) {
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(this.getErrorResponseInString(errors)), String.class);
    }

    protected String getErrorResponseInString(Errors errors) {
        List<Map<String, String>> responseErrors = new ArrayList<>();
        errors.getFieldErrors().forEach(error -> {
            Map<String, String> errorMapping = new HashMap<>();
            errorMapping.put("field", error.getField());
            errorMapping.put("code", error.getCode());
            errorMapping.put("message", error.getDefaultMessage());
            responseErrors.add(errorMapping);
        });
        return MessageUtils.writeValueAsString(responseErrors);
    }
}