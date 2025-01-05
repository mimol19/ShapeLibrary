package com.example.shapelibrary.business;

import com.example.shapelibrary.repository.UserRepository;
import com.example.shapelibrary.repository.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(String userName) {
        return userRepository.findByName(userName)
                .orElseGet(() -> {
                    return userRepository.save(User.builder().name(userName).build());
                });
    }
}
