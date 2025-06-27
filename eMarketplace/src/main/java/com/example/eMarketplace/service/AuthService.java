package com.example.eMarketplace.service;

import com.example.eMarketplace.dto.LoginDto;

import com.example.eMarketplace.dto.UserDto;
import com.example.eMarketplace.dto.UserLoginDto;
import com.example.eMarketplace.model.User;
import com.example.eMarketplace.util.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserDto user) {
        var encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userService.saveUser(user);
    }

    public Map<String, Object> authenticateUser(LoginDto user) {
        var login = user.getUsername() != null ? user.getUsername() : user.getEmail();
        UsernamePasswordAuthenticationToken auth = unauthenticated(login, user.getPassword());
        Authentication authentication = authenticationManager.authenticate(auth);
        User authUser = (User) authentication.getPrincipal();

        String token = tokenUtils.generateToken(authUser.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        UserLoginDto dto = new UserLoginDto();
        dto.setId(authUser.getId());
        dto.setUsername(authUser.getUsername());
        dto.setEmail(authUser.getEmail());
        dto.setBirthday(authUser.getBirthday());

        response.put("user", dto);

        return response;
    }


}
