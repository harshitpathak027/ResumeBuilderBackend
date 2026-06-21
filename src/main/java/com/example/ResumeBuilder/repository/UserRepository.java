package com.example.ResumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByName(String username);
    User findByNameIgnoreCaseOrEmailIgnoreCase(String name, String email);
}
