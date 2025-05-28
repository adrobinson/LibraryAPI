package com.example.Library.service.user;

import com.example.Library.dto.user.UpdateDetailsRequest;
import com.example.Library.dto.user.UpdatePasswordRequest;
import com.example.Library.dto.user.UserRegistrationDto;
import com.example.Library.dto.user.UserResponseDto;
import com.example.Library.entity.User;
import com.example.Library.exception.CredentialsAlreadyExistException;
import com.example.Library.repository.UserRepository;
import com.example.Library.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Library.jwt.JwtUtil;

import javax.management.relation.RoleNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private static final String ADMIN_USERNAME = "admin";
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public UserResponseDto saveUser(UserRegistrationDto dto){
        String normalizedEmail = StringUtil.normalizeString(dto.email);
        String normalizedUsername = StringUtil.normalizeString(dto.username);
        if(repository.existsByEmail(normalizedEmail)){
            throw new CredentialsAlreadyExistException("Email already exists");
        } else if(repository.existsByUsername(normalizedUsername)){
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

    public void updateAdminPassword(UpdatePasswordRequest request) {
        var user = getCurrentUser();
        if(!Objects.equals(user.getUsername(), ADMIN_USERNAME)){
            throw new AccessDeniedException("Only user 'admin' can use this request");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));

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

    public void deleteUser(Integer id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        repository.delete(user);
    }


    public User getCurrentUser(){
        // If the user is authenticated when the request reaches the jwt filter, they will be stored in the security context
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails){
            // pull the username out of the security context, then find the username in the DB
            return repository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Unexpected error, profile not found"));
        } else {
            throw new AccessDeniedException("No authenticated user found");
        }
    }

    public UserResponseDto getCurrent() {
        User user = getCurrentUser();
        return userMapper.toUserResponse(user);
    }

    public List<String> updateDetails(UpdateDetailsRequest request){
        User user = getCurrentUser();

        if (ADMIN_USERNAME.equals(user.getUsername())) {
            throw new AccessDeniedException("Cannot update this user");
        }

        List<String> messages = new ArrayList<>();

        boolean updated = updateUsernameIfNeeded(user, request.username, messages)
                | updateEmailIfNeeded(user, request.email, messages)
                | updatePasswordIfNeeded(user, request.password, messages);

        if(!updated){
            throw new IllegalArgumentException("No valid fields provided to update");
        }

        repository.save(user);
        return messages;

    }

    private boolean updateUsernameIfNeeded(User user, String newUsername, List<String> messages) {
        if (newUsername == null || newUsername.isBlank()) return false;

        String normalized = StringUtil.normalizeString(newUsername);
        if (!normalized.matches("^\\S+$")) {
            throw new IllegalArgumentException("Username cannot contain spaces");
        }
        if (repository.existsByUsername(normalized)) {
            throw new CredentialsAlreadyExistException("Username already exists");
        }

        String old = user.getUsername();
        user.setUsername(normalized);
        messages.add("Username updated: " + old + " -> " + normalized);
        return true;
    }

    private boolean updateEmailIfNeeded(User user, String newEmail, List<String> messages) {
        if (newEmail == null || newEmail.isBlank()) return false;

        String normalized = StringUtil.normalizeString(newEmail);
        if (repository.existsByEmail(normalized)) {
            throw new CredentialsAlreadyExistException("Email already exists");
        }

        String old = user.getEmail();
        user.setEmail(normalized);
        messages.add("Email updated: " + old + " -> " + normalized);
        return true;
    }

    private boolean updatePasswordIfNeeded(User user, String newPassword, List<String> messages) {
        if (newPassword == null || newPassword.isBlank()) return false;

        user.setPassword(passwordEncoder.encode(newPassword));
        messages.add("Password updated");
        return true;
    }

    public UserResponseDto deleteCurrent(){
        User user = getCurrentUser();

        if (Objects.equals(user.getUsername(), "admin")) {
            throw new AccessDeniedException("Cannot delete this user");
        }

        UserResponseDto dto = userMapper.toUserResponse(user);
        repository.delete(user);
        return dto;
    }

}
