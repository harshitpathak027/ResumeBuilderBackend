package com.example.ResumeBuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public String registerUser(User user) {
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

    private boolean isUsersPrimaryKeySequenceIssue(DataIntegrityViolationException exception) {
        String message = exception.getMostSpecificCause() != null
                ? exception.getMostSpecificCause().getMessage()
                : exception.getMessage();

        if (message == null) {
            return false;
        }

        String lower = message.toLowerCase();
        return lower.contains("users_pkey") || lower.contains("key (id)");
    }

    private void resetUsersIdSequence() {
        jdbcTemplate.execute(
                "SELECT setval(pg_get_serial_sequence('users','id'), COALESCE((SELECT MAX(id) FROM users), 0), true)");
    }
    
}
