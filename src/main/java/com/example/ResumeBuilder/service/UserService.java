package com.example.ResumeBuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.UserRepository;

@Service
public class UserService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("name, email and password are required");
        }

        if (isBlank(user.getName()) || isBlank(user.getEmail()) || isBlank(user.getPassword())) {
            throw new IllegalArgumentException("name, email and password are required");
        }

        if (!EMAIL_PATTERN.matcher(user.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("Please enter a valid email");
        }

        user.setName(user.getName().trim());
        user.setEmail(user.getEmail().trim());
        // user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // try {
            userRepository.save(user);
            return "User registered successfully";
        // } catch (DataIntegrityViolationException exception) {
        //     if (isUsersPrimaryKeySequenceIssue(exception)) {
        //         resetUsersIdSequence();
        //         userRepository.save(user);
        //         return "User registered successfully";
        //     }
        //     throw exception;
        // }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    
}
