package com.fitness.repository;

import com.fitness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>
{
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(Long phoneNumber);
}
