package com.newsletter.springboot.controller;

import com.newsletter.springboot.dto.LoginRequest;
import com.newsletter.springboot.dto.LoginResponse;
import com.newsletter.springboot.dto.RefreshTokenRequest;
import com.newsletter.springboot.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final InMemoryUserDetailsManager userDetailsManager;

    public AuthController(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            InMemoryUserDetailsManager userDetailsManager
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userDetailsManager = userDetailsManager;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        return new LoginResponse(
                tokenService.generateAccessToken(authentication),
                tokenService.generateRefreshToken(authentication),
                "Bearer",
                tokenService.getAccessTokenExpiration()
        );
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody RefreshTokenRequest request) {
        Jwt jwt = tokenService.decodeToken(request.refreshToken());

        String tokenType = jwt.getClaimAsString("type");

        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("Token invalide : refresh token attendu");
        }

        String username = jwt.getSubject();

        var userDetails = userDetailsManager.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );

        return new LoginResponse(
                tokenService.generateAccessToken(authentication),
                request.refreshToken(),
                "Bearer",
                tokenService.getAccessTokenExpiration()
        );
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logout simulé : côté client, supprime les tokens.";
    }
}