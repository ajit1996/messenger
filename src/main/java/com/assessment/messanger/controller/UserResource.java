package com.assessment.messanger.controller;

import com.assessment.messanger.exception.ValidationException;
import com.assessment.messanger.entity.User;
import com.assessment.messanger.repository.UserRepository;
import com.assessment.messanger.utils.constants.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> createUser(@RequestBody User request) {
        if(Objects.isNull(request)){
            throw new ValidationException(UserConstants.USER_NOT_NULL_ERROR_MSG);
        } else if (ObjectUtils.isEmpty(request.getUsername()) || ObjectUtils.isEmpty(request.getPassword())) {
            throw new ValidationException(UserConstants.USERNAME_AND_PASSWORD_NOT_EMPTY_ERROR_MSG);
        } else{
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            request.setPassword(encodedPassword);
        }
        return userRepository.save(request);
    }
}
