package com.fitness.service;

import com.fitness.DTOs.UserMapper;
import com.fitness.DTOs.UserRegistrationRequest;
import com.fitness.DTOs.UserResponse;
import com.fitness.model.User;
import com.fitness.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService
{

    private final UserRepository userRepository;

    private final UserMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository,  UserMapper mapper)
    {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    public UserResponse register(@Valid UserRegistrationRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("An User with Email is already exists, try different email.");
        }

        if(userRepository.existsByPhoneNumber(request.getPhoneNumber()))
        {
            throw new RuntimeException("An User with Phone Number is already exists, try different phone number.");
        }

        User user = mapper.toEntity(request);
        userRepository.save(user);

        UserResponse userResponse = mapper.toResponse(user);

        return userResponse;
    }

    public UserResponse getUserProfile(String userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapper.toResponse(user);
    }

    public Boolean existsByUserId(String userId)
    {
        log.info("Calling User Validation API for userId: {}", userId);
        return userRepository.existsById(userId);
    }
}
