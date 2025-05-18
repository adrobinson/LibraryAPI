package com.example.Library.service;

import com.example.Library.dto.UserRegistrationDto;
import com.example.Library.dto.UserResponseDto;
import com.example.Library.entity.User;
import com.example.Library.exception.CredentialsAlreadyExistException;
import com.example.Library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Library.jwt.JwtUtil;
import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public UserResponseDto saveUser(UserRegistrationDto dto){

        if(repository.existsByEmail(dto.email)){
            throw new CredentialsAlreadyExistException("Email already exists");
        } else if(repository.existsByUsername(dto.username)){
            throw new CredentialsAlreadyExistException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(dto.password);
        var user = userMapper.mapRegisterToUser(dto);
        user.setPassword(encodedPassword);
        repository.save(user);
        return userMapper.toUserResponse(user);
    }

    public String authenticateLoginRequest (String username, String password){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = jwtUtil.generateToken((UserDetails) auth.getPrincipal());

        return token;
    }

    public List<UserResponseDto> findAllUsers(){
        return repository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponseDto findUserById(Integer id){
        return userMapper.toUserResponse(repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("id provided belongs to no user")));
    }

    public void updateUserRole(Integer id, String newRole) throws RoleNotFoundException {
        if(!Objects.equals(newRole, "ROLE_ADMIN") && !Objects.equals(newRole, "ROLE_USER")){
            throw new RoleNotFoundException("The role provided does not exist");
        }

        User user = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("id provided belongs to no user"));

        if(Objects.equals(user.getUsername(), "admin")){
            throw new UsernameNotFoundException("Cannot update this user role");
        }

        user.setRole(newRole);
        repository.save(user);
    }

    public UserResponseDto deleteUser(Integer id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        repository.delete(user);
        return userMapper.toUserResponse(user);
    }

}
