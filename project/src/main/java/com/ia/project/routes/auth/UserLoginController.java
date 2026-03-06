package com.ia.project.routes.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.project.DTO.auth.LoginRequestDTO;
import com.ia.project.service.auth.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> login (@RequestBody LoginRequestDTO request) {
        String response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }

}
