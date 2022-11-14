package com.assessment.messanger.service;

import com.assessment.messanger.exception.UnauthorizedException;
import com.assessment.messanger.repository.UserRepository;
import com.assessment.messanger.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username).switchIfEmpty(Mono.defer(() -> Mono.error(new UnauthorizedException("User doesn't exists!")))).map(user -> userUtils.toAuthUser(user.getUsername(), user.getPassword()));
    }
}