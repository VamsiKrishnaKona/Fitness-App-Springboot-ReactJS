package com.fitness.DTOs;

import com.fitness.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper
{
    public User toEntity(UserRegistrationRequest request)
    {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }

    public UserResponse toResponse(User user)
    {
        UserResponse response = new UserResponse();
        response.setId(user.getId().toString());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setPassword(user.getPassword());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedOn(user.getCreatedOn());
        response.setUpdatedOn(user.getUpdatedOn());
        return response;
    }
}
