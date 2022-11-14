package com.assessment.messanger.utils;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserUtils {

    public UserDetails toAuthUser(String username, String password) {
        return User.withUsername(username).password(password).authorities(Collections.emptyList()).build();
    }
}
