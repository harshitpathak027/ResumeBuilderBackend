package com.example.ResumeBuilder.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.ProjectTech;

@Repository
public interface ProjectTechRepository extends JpaRepository<ProjectTech, UUID>{
	void deleteByProjectId(UUID projectId);
    
}
