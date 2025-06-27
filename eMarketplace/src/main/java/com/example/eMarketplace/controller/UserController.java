package com.example.eMarketplace.controller;

import com.example.eMarketplace.dto.LoginDto;
import com.example.eMarketplace.dto.UserDto;
import com.example.eMarketplace.service.AuthService;
import com.example.eMarketplace.service.UserService;
import com.example.eMarketplace.util.CredentialsValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/page")
    public String index() {
        return "login";
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto user) {
        System.out.println("→ username = " + user.getUsername());
        System.out.println("→ email = " + user.getEmail());
        System.out.println("→ password = " + user.getPassword());
        System.out.println("→ birthday = " + user.getBirthday());
        if (!CredentialsValidator.validate(user.getUsername(), user.getEmail(), user.getPassword(), user.getBirthday())) {
            return ResponseEntity.badRequest().build();
        }
        authService.createUser(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto user) {
        return ResponseEntity.ok(authService.authenticateUser(user));
    }

}
