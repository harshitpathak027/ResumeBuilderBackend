package com.example.ResumeBuilder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.model.User;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long>{

    List<Resume> findByUserId(Long userid);
    
}
