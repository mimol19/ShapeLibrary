package com.example.shapelibrary;

import com.example.shapelibrary.business.UserService;
import com.example.shapelibrary.repository.ShapeRepository;
import com.example.shapelibrary.repository.UserRepository;
import com.example.shapelibrary.repository.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnExistingUser() {
        String userName = "John";
        User existingUser = User.builder().name(userName).build();
        when(userRepository.findByName(userName)).thenReturn(Optional.of(existingUser));

        User result = userService.getOrCreateUser(userName);

        assertEquals(existingUser, result);
        verify(userRepository, times(1)).findByName(userName);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void shouldCreateNewUserWhenNotExist() {
        String userName = "Jane";

        when(userRepository.findByName(userName)).thenReturn(Optional.empty());
        User user = User.builder().name(userName).build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.getOrCreateUser(userName);

        assertNotNull(result);
        assertEquals(userName, result.getName());
        verify(userRepository, times(1)).findByName(userName);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
