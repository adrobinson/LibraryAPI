package com.example.Library.controller;


import com.example.Library.dto.UserLoginDto;
import com.example.Library.dto.UserRegistrationDto;
import com.example.Library.dto.UserResponseDto;
import com.example.Library.entity.User;
import com.example.Library.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserController {

    private UserService userService;

    @PostMapping("/user/register")
    public UserResponseDto registerUser(@Valid @RequestBody UserRegistrationDto dto){
        return userService.saveUser(dto);
    }

    @PostMapping("/login")
    public User loginRequest(@Valid @RequestBody UserLoginDto dto){
        var user = userService.findUserByIdentifier(dto.identifier);
        return userService.validatePassword(dto.password, user.getPassword()) ? user : null;
    }


}
