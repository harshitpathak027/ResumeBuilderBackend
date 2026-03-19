package com.example.ResumeBuilder.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill,UUID>{
    
}
