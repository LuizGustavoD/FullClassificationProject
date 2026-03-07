package com.ia.project.service.auth;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ia.project.DTO.auth.LoginRequestDTO;
import com.ia.project.DTO.auth.RegisterRequestDTO;
import com.ia.project.model.auth.User;
import com.ia.project.repository.UserRepository;
import com.ia.project.security.JWT.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final JwtService jwtService;

    public String registerUser(RegisterRequestDTO request) {

        if (userRepository.findByUsername(request.username()) != null) {
            return "Username already exists";
        }
        
        var user = new com.ia.project.model.auth.User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        userRepository.save(user);

        String token = jwtService.generatevValidationToken(user);
        
        String confirmationLink = "http://localhost:8080/auth/register/confirm/" + user.getId() + "?token=" + token;
        String email = user.getEmail();
        HashMap<String, String> payload = new HashMap<>();

        payload.put("recipient", email);
        payload.put("confirmation_link", confirmationLink);


        restTemplate.postForObject("http://localhost:5001/send_mail", payload, String.class);

        return "User registered successfully";
    }

    public String loginUser(LoginRequestDTO request) {

        if (userRepository.findByUsername(request.username()) == null) {
            return "User not found";
        }

        if (!userRepository.findByUsername(request.username()).isConfirmed()) {
            return "User not confirmed";
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return token;
    }

    public String confirmUser(java.util.UUID uuid, String token) {

        if (!jwtService.validateToken(token)) {
            return "Invalid token";
        }

        var user = userRepository.findById(uuid);

        if (user.isEmpty()) {
            return "User not found";
        }
        
        var existingUser = user.get();
        existingUser.setConfirmed(true);
        userRepository.save(existingUser);

        String newToken = jwtService.generateToken(existingUser);

        return newToken;
    }

}
