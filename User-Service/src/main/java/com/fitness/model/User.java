package com.fitness.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true,  nullable = false,  length = 50)
    private String email;

    @Column(unique = true,  nullable = false,  length = 12)
    private Long phoneNumber;

    @Column(length = 32, nullable = false)
    private String password;

    @Column(length = 32, nullable = false)
    private String firstName;

    @Column(length = 32,  nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

}
