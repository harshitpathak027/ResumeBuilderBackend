package com.example.ResumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<com.example.ResumeBuilder.model.Template, Long>{
    
}
