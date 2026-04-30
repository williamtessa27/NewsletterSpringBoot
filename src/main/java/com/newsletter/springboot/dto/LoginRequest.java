package com.newsletter.springboot.dto;

public record LoginRequest(
        String username,
        String password
) {
}