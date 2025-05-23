package com.example.Library.service;

import com.example.Library.dto.book.UserBookListDto;
import com.example.Library.dto.user.UserResponseDto;
import com.example.Library.entity.User;
import com.example.Library.dto.user.UserRegistrationDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User mapRegisterToUser(UserRegistrationDto dto){
        var user= new User();
        user.setUsername((dto.username).toLowerCase().trim());
        user.setPassword(dto.password);
        user.setEmail((dto.email).toLowerCase());
        user.setRole("ROLE_USER");
        return user;
    }

    public UserResponseDto toUserResponse(User user){
        var userResponse = new UserResponseDto();
        userResponse.username = user.getUsername();
        userResponse.email = user.getEmail();
        return userResponse;
    }

    public UserBookListDto getUserBookList(User user){
        UserBookListDto dto = new UserBookListDto();
        dto.bookList = user.getBooks();
        return dto;
    }
}
