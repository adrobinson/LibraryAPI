package com.example.Library.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    //TODO move logic to user service, create response dto

    public UserController(UserRepository repository, PasswordEncoder encoder, UserMapper mapper) {
        this.repository = repository;
        this.passwordEncoder = encoder;
        this.userMapper = mapper;
    }

    @PostMapping("/user/register")
    public User registerUser(@RequestBody UserRegistrationDto registrationDto){
        String encodedPassword = passwordEncoder.encode(registrationDto.password);
        var user = userMapper.mapRegisterToUser(registrationDto);
        user.setPassword(encodedPassword);
        return repository.save(user);
    }


}
