package com.example.ResumeBuilder.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>{
    
}
