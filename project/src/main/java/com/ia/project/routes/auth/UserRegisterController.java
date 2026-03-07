package com.ia.project.routes.auth;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/confirm/{uuid}")
    public ResponseEntity<String> confirmUser(
            @PathVariable UUID uuid,
            @RequestParam String token) {

        String response = userService.confirmUser(uuid, token);

        return ResponseEntity.ok(response);
    }

}
