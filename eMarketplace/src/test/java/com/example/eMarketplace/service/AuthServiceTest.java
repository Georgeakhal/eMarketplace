package com.example.eMarketplace.service;


import com.example.eMarketplace.config.TestApplicationConfiguration;
import com.example.eMarketplace.dto.LoginDto;
import com.example.eMarketplace.dto.UserDto;
import com.example.eMarketplace.model.User;
import com.example.eMarketplace.util.TokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@Import(TestApplicationConfiguration.class)
class AuthServiceTest {

    @MockitoSpyBean
    private UserService userService;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoSpyBean
    private TokenUtils tokenUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Test
    void testCreateUser() {
        UserDto userDto = UserDto.builder().password("password").build();

        authService.createUser(userDto);

        verify(userService).saveUser(userDto);
    }

    @Test
    void testAuthenticateUser() {
        LoginDto loginDto = LoginDto.builder().username("user").password("password").build();
        User user = User.builder().username("user").password("password").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        authService.authenticateUser(loginDto);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenUtils).generateToken(user.getUsername());
    }

    @Test
    void testAuthenticateUserIncorrectPassword() {
        LoginDto loginDto = LoginDto.builder().username("user").password("password").build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect password"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () ->
                authService.authenticateUser(loginDto));

        assertEquals("Incorrect password", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenUtils, never()).generateToken(loginDto.getUsername());
    }



}
