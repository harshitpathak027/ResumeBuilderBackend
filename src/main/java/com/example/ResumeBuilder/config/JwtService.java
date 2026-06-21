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

import com.example.ResumeBuilder.model.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long ttlHours;
    private final boolean disableExpiration;

    public JwtService(
        @Value("${app.jwt.secret:resume-builder-super-secret-key-change-me-1234567890}") String secret,
        @Value("${app.jwt.ttlHours:10}") long ttlHours,
        @Value("${app.jwt.disableExpiration:false}") boolean disableExpiration) {
        this.signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.ttlHours = ttlHours;
        this.disableExpiration = disableExpiration;
    }

    public String generateToken(Authentication authentication) {
        Date issuedAt = new Date();

        var builder = Jwts.builder()
            .subject(authentication.getName())
            .issuedAt(issuedAt);

        if (!disableExpiration && ttlHours > 0) {
            Date expiry = new Date(issuedAt.getTime() + TimeUnit.HOURS.toMillis(ttlHours));
            builder.expiration(expiry);
        }

        return builder
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

            boolean subjectMatches = username.equals(userDetails.getUsername());

            // Backward compatibility: older tokens used name/email as subject.
            if (!subjectMatches && userDetails instanceof UserPrincipal principal) {
                User user = principal.getUser();
                if (user != null) {
                    String email = user.getEmail();
                    String name = user.getName();
                    if (email != null && username.equalsIgnoreCase(email.trim())) {
                        subjectMatches = true;
                    } else if (name != null && username.equalsIgnoreCase(name.trim())) {
                        subjectMatches = true;
                    }
                }
            }

            if (!subjectMatches) {
                return false;
            }

            // If expiration is disabled or token has no exp claim, treat it as non-expiring.
            if (disableExpiration || expiration == null) {
                return true;
            }

            return expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
    
}
