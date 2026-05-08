package com.newsletter.springboot.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**")
                                                .disable())
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.disable()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/h2-console/**", "/api/auth/login",
                                                                "/api/auth/refresh")
                                                .permitAll()

                                                .requestMatchers(HttpMethod.GET, "/api/tasks/**")
                                                .hasAnyRole("USER", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/tasks/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasRole("ADMIN")

                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

                return http.build();
        }

        @Bean
        public JwtEncoder jwtEncoder() {
                SecretKeySpec secretKey = new SecretKeySpec(
                                jwtSecret.getBytes(StandardCharsets.UTF_8),
                                "HmacSHA256");

                return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        }

        @Bean
        public JwtDecoder jwtDecoder() {
                SecretKeySpec secretKey = new SecretKeySpec(
                                jwtSecret.getBytes(StandardCharsets.UTF_8),
                                "HmacSHA256");

                return NimbusJwtDecoder.withSecretKey(secretKey).build();
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
                var admin = User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin123"))
                                .roles("ADMIN")
                                .build();

                var user = User.builder()
                                .username("user")
                                .password(passwordEncoder.encode("user123"))
                                .roles("USER")
                                .build();

                return new InMemoryUserDetailsManager(admin, user);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }
}