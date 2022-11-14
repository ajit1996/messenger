package com.assessment.messanger.validator;

import com.assessment.messanger.entity.Message;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MessageRequestEntityValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Message.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "topic", "field.required", null, "topic cannot be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "field.required", null, "content cannot be empty");
    }
}

