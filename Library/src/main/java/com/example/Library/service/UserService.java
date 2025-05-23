package com.example.Library.service;

import com.example.Library.dto.book.BookRequestDto;
import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.dto.user.UpdateDetailsRequest;
import com.example.Library.dto.user.UpdatePasswordRequest;
import com.example.Library.dto.user.UserRegistrationDto;
import com.example.Library.dto.user.UserResponseDto;
import com.example.Library.entity.Book;
import com.example.Library.entity.User;
import com.example.Library.exception.CredentialsAlreadyExistException;
import com.example.Library.repository.UserRepository;
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
import java.util.ArrayList;
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
        if(repository.existsByEmail((dto.email).toLowerCase())){
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

    public void updateAdminPassword(UpdatePasswordRequest request) {
        var user = getCurrentUser();
        if(!Objects.equals(user.getUsername(), "admin")){
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

    public UserResponseDto deleteUser(Integer id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        repository.delete(user);
        return userMapper.toUserResponse(user);
    }


    private User getCurrentUser(){
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

        if (Objects.equals(user.getUsername(), "admin")) {
            throw new AccessDeniedException("Cannot update this user");
        }

        boolean updated = false;
        List<String> messages = new ArrayList<>();

        if(request.username != null && !request.username.isBlank()) {
            String normalizedUsername = (request.username).toLowerCase().trim();
            if(repository.existsByUsername((normalizedUsername))){
                throw new CredentialsAlreadyExistException("Username already exists");
            }
            if (!normalizedUsername.matches("^\\S+$")) {
                throw new IllegalArgumentException("Username cannot contain spaces");
            }
            String oldUsername = user.getUsername();
            user.setUsername(normalizedUsername);
            messages.add("Username updated: " + oldUsername + " -> " + normalizedUsername);
            updated = true;
        }

        if(request.email != null && !request.email.isBlank()) {
            String normalizedEmail = (request.email).toLowerCase().trim();
            if(repository.existsByEmail(normalizedEmail)){
                throw new CredentialsAlreadyExistException("Email already exists");
            }
            String oldEmail = user.getEmail();
            user.setEmail(normalizedEmail);
            messages.add("Email updated: " + oldEmail + " -> " + normalizedEmail);
            updated = true;
        }

        if(request.password != null && !request.password.isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password));
            messages.add("Password updated");
            updated = true;
        }

        if(!updated){
            throw new IllegalArgumentException("No valid fields provided to update");
        }

        repository.save(user);
        return messages;

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

    BookService bookService;

    public List<BookResponseDto> addToBookList(BookRequestDto dto){
        User user = getCurrentUser();
        boolean updated = false;

        if(dto.bookId != null){
            Book book = bookService.findBookById(dto.bookId);
            user.addBook(book);
            updated = true;
        }

        if(dto.bookIds != null && !dto.bookIds.isEmpty()){
            for(Integer id: dto.bookIds){
                Book book = bookService.findBookById(id);
                user.addBook(book);
            }
            updated = true;
        }

        if(dto.bookTitle != null && !dto.bookTitle.trim().isEmpty()){
            String normalizedTitle = dto.bookTitle.toLowerCase();
            Book book = bookService.findBookByTitle(normalizedTitle);
            user.addBook(book);
            updated = true;
        }

        if(dto.bookTitles != null && !dto.bookTitles.isEmpty()){
            for(String title: dto.bookTitles){
                String normalizedTitle = title.toLowerCase();
                Book book = bookService.findBookByTitle(normalizedTitle);
                user.addBook(book);
            }
            updated = true;
        }

        if(!updated){
            throw new IllegalArgumentException("No valid field provided, valid fields include:\nbookId : Integer id\nbookIds: [Integer id]\nbookTitle: String title\nbookTitles: [String titles]");
        }

        repository.save(user);
        return userMapper.getUserBookList(user);

    }

    public List<BookResponseDto> getBookListOfCurrent(){
        User user = getCurrentUser();
        return userMapper.getUserBookList(user);
    }

}
