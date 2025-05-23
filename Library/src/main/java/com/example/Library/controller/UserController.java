package com.example.Library.controller;


import com.example.Library.dto.book.BookRequestDto;
import com.example.Library.dto.book.UserBookListDto;
import com.example.Library.dto.user.*;
import com.example.Library.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@AllArgsConstructor
@RestController
public class UserController {

    private UserService userService;

    // Public Endpoints

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

    // Admin Endpoints

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/admin/password")
    public ResponseEntity<String> updateAdminPassword(@Valid @RequestBody UpdatePasswordRequest request){
        userService.updateAdminPassword(request);
        return ResponseEntity.ok()
                .body("Password updated");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getUserById(@PathVariable("user-id") Integer id) { return ResponseEntity.ok(userService.findUserById(id));}

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/users/{user-id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable ("user-id") Integer id,
            @RequestBody UpdateRoleRequest request
    ) throws RoleNotFoundException {
        userService.updateUserRole(id, request.role);
        return ResponseEntity.ok("User role updated to " + request.role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{user-id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user-id") Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }

    // User Endpoints

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrent());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/users/me")
    public ResponseEntity<?> updateDetails(@Valid @RequestBody UpdateDetailsRequest request){
        var response = userService.updateDetails(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/users/me")
    public ResponseEntity<?> deleteCurrentUser(){
        var response = userService.deleteCurrent();
        return ResponseEntity.ok()
                .body(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/users/me/books")
    public ResponseEntity<UserBookListDto> addToBookList(@RequestBody BookRequestDto dto){
        return ResponseEntity.ok(userService.addToBookList(dto));
    }


}
