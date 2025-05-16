package com.example.Library.service;

import com.example.Library.dto.UserRegistrationDto;
import com.example.Library.dto.UserResponseDto;
import com.example.Library.entity.User;
import com.example.Library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserResponseDto saveUser(UserRegistrationDto dto){
        String encodedPassword = passwordEncoder.encode(dto.password);
        var user = userMapper.mapRegisterToUser(dto);
        user.setPassword(encodedPassword);
        repository.save(user);
        return userMapper.toUserResponse(user);
    }

    public User findUserByIdentifier(String identifier){
        if(identifier.contains("@")) {
            return repository.findByEmail(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        } else {
            return repository.findByUsername(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        }
    }

    public boolean validatePassword(String rawPassword, String encodedPassword){
        if(passwordEncoder.matches(rawPassword, encodedPassword)) {
            return true;
        } else {
            throw new BadCredentialsException("Incorrect password");
        }
    }
}
