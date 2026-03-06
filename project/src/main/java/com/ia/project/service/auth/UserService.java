package com.ia.project.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ia.project.DTO.auth.LoginRequestDTO;
import com.ia.project.DTO.auth.RegisterRequestDTO;
import com.ia.project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String registerUser(RegisterRequestDTO request) {

        if (userRepository.findByUsername(request.username()) != null) {
            return "Username already exists";
        }
        var user = new com.ia.project.model.auth.User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        userRepository.save(user);
        return "User registered successfully";

    }

    public String loginUser(LoginRequestDTO request) {
        var user = userRepository.findByUsername(request.username());
        if (user == null) {
            return "User not found";
        }
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return "Invalid password";
        }

        return "User logged in successfully";
    }

}
