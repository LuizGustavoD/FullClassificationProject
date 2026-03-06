package com.ia.project.routes.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.project.DTO.auth.RegisterRequestDTO;
import com.ia.project.service.auth.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/register")
@RequiredArgsConstructor
public class UserRegisterController {
    
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        String response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }


}
