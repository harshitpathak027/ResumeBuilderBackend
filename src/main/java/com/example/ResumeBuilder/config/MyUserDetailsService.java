package com.example.ResumeBuilder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = username == null ? "" : username.trim();
        User user = userRepository.findByNameIgnoreCaseOrEmailIgnoreCase(normalized, normalized);
        if(user == null){
            throw new UsernameNotFoundException("User not found"); 
        }
        return new UserPrincipal(user);
    }
    



}
