package com.example.ResumeBuilder.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>{
	List<Project> findByResumeId(Long resumeId);
	void deleteByResumeId(Long resumeId);
    
}
