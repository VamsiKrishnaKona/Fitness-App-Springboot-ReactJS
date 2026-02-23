package com.fitness.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse
{
    private String id;
    private String email;
    private Long phoneNumber;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
