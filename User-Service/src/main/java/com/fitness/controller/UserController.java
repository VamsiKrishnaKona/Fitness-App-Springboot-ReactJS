package com.fitness.controller;

import com.fitness.DTOs.UserRegistrationRequest;
import com.fitness.DTOs.UserResponse;
import com.fitness.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")

@AllArgsConstructor
public class UserController
{
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId)
    {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> userRegistration(@Valid @RequestBody UserRegistrationRequest request)
    {
        return ResponseEntity.ok(userService.register(request));
    }
}
