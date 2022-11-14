package com.assessment.messanger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OperationNotAllowed extends ResponseStatusException {
    public OperationNotAllowed(String message) {
        super(HttpStatus.NOT_ACCEPTABLE, message);
    }
}
