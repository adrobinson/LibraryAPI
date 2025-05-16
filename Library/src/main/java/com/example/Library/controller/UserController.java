package com.example.Library.controller;


import com.example.Library.dto.UserLoginDto;
import com.example.Library.dto.UserRegistrationDto;
import com.example.Library.dto.UserResponseDto;
import com.example.Library.entity.User;
import com.example.Library.service.UserMapper;
import com.example.Library.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {

    private UserService userService;
    private UserMapper userMapper;

    @PostMapping("/user/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegistrationDto dto){
        UserResponseDto repsonse = userService.saveUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(repsonse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginRequest(@Valid @RequestBody UserLoginDto dto){
        String token = userService.authenticateLoginRequest(dto.identifier, dto.password);
        return ResponseEntity.ok(token);
    }


}
