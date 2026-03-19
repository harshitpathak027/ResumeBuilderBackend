package com.example.ResumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.Personal;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long>{
    
}
