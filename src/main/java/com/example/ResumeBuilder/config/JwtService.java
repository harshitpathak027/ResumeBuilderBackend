package com.example.ResumeBuilder.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private final SecretKey signingKey;

    public JwtService(
        @Value("${app.jwt.secret:resume-builder-super-secret-key-change-me-1234567890}") String secret) {
        this.signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public String generateToken(Authentication authentication) {
        Date issuedAt = new Date();
        Date expiry = new Date(issuedAt.getTime() + TimeUnit.HOURS.toMillis(10));

        return Jwts.builder()
            .subject(authentication.getName())
            .issuedAt(issuedAt)
            .expiration(expiry)
            .signWith(signingKey)
            .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (JwtException | IllegalArgumentException exception) {
            return null;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            if (username == null || userDetails == null) {
                return false;
            }

            Date expiration = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

            return username.equals(userDetails.getUsername())
                && expiration != null
                && expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
    
}
