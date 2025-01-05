package com.example.shapelibrary.domain;

import com.example.shapelibrary.repository.UserRepository;
import com.example.shapelibrary.repository.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(String userName) {
        return userRepository.findByName(userName)
                .map(user -> {
                    log.info("User found: ID={}, Name={}", user.getId(), user.getName());
                    return user;
                })
                .orElseGet(() -> {
                    User newUser = userRepository.save(User.builder().name(userName).build());
                    log.info("User not found, new user created successfully: ID={}, Name={}",
                            newUser.getId(), newUser.getName());
                    return newUser;
                });
    }
}