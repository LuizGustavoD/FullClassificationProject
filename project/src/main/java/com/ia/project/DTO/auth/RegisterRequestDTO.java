package com.ia.project.DTO.auth;

public record RegisterRequestDTO(
    String username,
    String password,
    String email
) {
    
}
