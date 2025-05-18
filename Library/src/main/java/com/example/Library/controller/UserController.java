package com.example.Library.controller;


import com.example.Library.dto.UpdateRoleRequest;
import com.example.Library.dto.UserLoginDto;
import com.example.Library.dto.UserRegistrationDto;
import com.example.Library.dto.UserResponseDto;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')") // TODO make this work and also add role hierarchy
    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')") // TODO make this work and also add role hierarchy
    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getUserById(@PathVariable("user-id") Integer id) { return ResponseEntity.ok(userService.findUserById(id));}

    @PreAuthorize("hasRole('ROLE_ADMIN')") // TODO make this work and also add role hierarchy
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




}
