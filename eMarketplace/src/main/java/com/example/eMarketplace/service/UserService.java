package com.example.eMarketplace.service;

import com.example.eMarketplace.dto.UserDto;
import com.example.eMarketplace.model.Post;
import com.example.eMarketplace.model.User;
import com.example.eMarketplace.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User getById(UUID id) {
        return userRepository.getReferenceById(id);
    }

    public Optional<User> getUser(String username) {
        return userRepository.findUserByUsernameOrEmail(username, username);
    }

    public void saveUser(UserDto userDto) {
        userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .birthday(userDto.getBirthday())
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

