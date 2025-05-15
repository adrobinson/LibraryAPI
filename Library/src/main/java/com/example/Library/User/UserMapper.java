package com.example.Library.User;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User mapRegisterToUser(UserRegistrationDto dto){
        var user= new User();
        user.setUsername(dto.username);
        user.setPassword(dto.password);
        user.setEmail(dto.email);
        user.setRole("USER");
        return user;
    }
}
