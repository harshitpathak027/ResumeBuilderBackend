package com.example.ResumeBuilder.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, UUID>{
    
}
