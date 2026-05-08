package com.newsletter.springboot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateAccessToken(Authentication authentication) {
        Instant now = Instant.now();

        String roles = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("newsletter-spring-boot")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiration))
                .subject(authentication.getName())
                .claim("scope", roles)
                .claim("type", "access")
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    public String generateRefreshToken(Authentication authentication) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("newsletter-spring-boot")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshTokenExpiration))
                .subject(authentication.getName())
                .id(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
}