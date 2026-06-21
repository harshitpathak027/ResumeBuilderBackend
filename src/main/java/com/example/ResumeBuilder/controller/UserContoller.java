package com.example.ResumeBuilder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ResumeBuilder.config.JwtService;
import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.UserRepository;
import com.example.ResumeBuilder.service.AccountDeletionService;
import com.example.ResumeBuilder.service.UserService;

@RestController
public class UserContoller {

    private static final Logger logger = LoggerFactory.getLogger(UserContoller.class);
    
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountDeletionService accountDeletionService;


    @PostMapping("/register")
    public String registerUser(@RequestBody User user){
        return userService.registerUser(user);  
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){
        String loginIdentifier = user.getName();
        if (loginIdentifier == null || loginIdentifier.trim().isEmpty()) {
            loginIdentifier = user.getEmail();
        }

        if (loginIdentifier == null || loginIdentifier.trim().isEmpty() || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "name/email and password are required"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginIdentifier.trim(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authentication);
                User loggedInUser = userRepository.findByNameIgnoreCaseOrEmailIgnoreCase(authentication.getName(), authentication.getName());

                Map<String, Object> userPayload = loggedInUser == null
                        ? Map.of("name", authentication.getName())
                        : Map.of(
                                "id", loggedInUser.getId(),
                                "name", loggedInUser.getName() == null ? "" : loggedInUser.getName(),
                                "email", loggedInUser.getEmail() == null ? "" : loggedInUser.getEmail());

                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "user", userPayload));
            }
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username/email or password"));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username/email or password"));
        }

    }

    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().trim().isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }

        String identifier = authentication.getName().trim();
        User loggedInUser = userRepository.findByNameIgnoreCaseOrEmailIgnoreCase(identifier, identifier);

        if (loggedInUser == null) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        return ResponseEntity.ok(Map.of(
                "id", loggedInUser.getId(),
                "name", loggedInUser.getName() == null ? "" : loggedInUser.getName(),
                "email", loggedInUser.getEmail() == null ? "" : loggedInUser.getEmail()));
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<?> deleteCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().trim().isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }

        logger.info("DELETE /users/me requested for {}", authentication.getName());
        boolean deleted = accountDeletionService.deleteAccountByLoginIdentifier(authentication.getName());
        if (!deleted) {
            logger.info("DELETE /users/me no user found for {}", authentication.getName());
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        logger.info("DELETE /users/me completed for {}", authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
