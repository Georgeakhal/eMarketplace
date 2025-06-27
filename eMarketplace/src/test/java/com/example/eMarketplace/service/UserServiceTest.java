package com.example.eMarketplace.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.example.eMarketplace.dto.UserDto;
import com.example.eMarketplace.model.User;
import com.example.eMarketplace.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}

    @Test
    void testGetUser() {
        var username = "giorgi123";
        var email = "giorgiakhaladze777@gmail.com";
        var password = "123";
        var user = User.builder()
                .id(UUID.fromString("51d30049-4018-4740-b776-29bb10a38967"))
                .username(username)
                .email(email)
                .password(password)
                .birthday(Date.valueOf(LocalDate.of(2008, 2, 1)))
                .build();

        when(userRepository.findUserByUsernameOrEmail(eq(username), any(String.class)))
                .thenReturn(Optional.of(user));

        var userOpt = userService.getUser(username);

        assertTrue(userOpt.isPresent());
        assertEquals(user, userOpt.get());
    }

    @Test
    void saveUser() {
        var username = "davit1235";
        var email = "davit@gmail.com";
        var password = "123";
        var userDto = UserDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .birthday(Date.valueOf(LocalDate.of(2003, 12, 5)))
                .build();

        var savedUser = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .password(password)
                .birthday(userDto.getBirthday())
                .build();


        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.saveUser(userDto);


        when(userRepository.findUserByUsernameOrEmail(eq(username), any()))
                .thenReturn(Optional.of(savedUser));

        var userOpt = userService.getUser(username);
        assertTrue(userOpt.isPresent());
        assertEquals(username, userOpt.get().getUsername());

        doNothing().when(userRepository).delete(any(User.class));

        userService.delete(savedUser);

        when(userRepository.findUserByUsernameOrEmail(eq(username), any()))
                .thenReturn(Optional.empty());

        var deletedUserOpt = userService.getUser(username);
        assertTrue(deletedUserOpt.isEmpty());
    }

}