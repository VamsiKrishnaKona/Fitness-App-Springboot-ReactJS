package com.fitness.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest
{
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    //@NotBlank(message = "mobile number is required")
    private Long phoneNumber;

    @NotBlank(message = "password is required")
    @Size(min = 1, max = 32, message = "Password must have least 12 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
}
