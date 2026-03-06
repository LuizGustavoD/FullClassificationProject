package com.ia.project.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ia.project.model.auth.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    
} 
