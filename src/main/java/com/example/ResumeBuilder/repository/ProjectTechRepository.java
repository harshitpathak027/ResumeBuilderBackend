package com.example.ResumeBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.ProjectTech;

@Repository
public interface ProjectTechRepository extends JpaRepository<ProjectTech,Long>{
    
}
