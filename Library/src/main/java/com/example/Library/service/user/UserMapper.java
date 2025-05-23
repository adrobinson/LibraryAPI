package com.example.Library.service.user;

import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.dto.user.UserResponseDto;
import com.example.Library.entity.User;
import com.example.Library.dto.user.UserRegistrationDto;
import com.example.Library.service.book.BookMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
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

    BookMapper bookMapper;

    public List<BookResponseDto> getUserBookList(User user){

        return user.getBooks()
                .stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }
}
