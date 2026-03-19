package com.example.ResumeBuilder.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.ResumeBuilder.model.Education;
import com.example.ResumeBuilder.repository.EducationRepository;


@Service
public class EducationService {
    @Autowired
    EducationRepository educationRepository;

    public Education addEducation(@RequestBody Education education) {
        // TODO Auto-generated method stub
        return educationRepository.save(education);

        }

    public List<Education>  getEducation() {
        // TODO Auto-generated method stub
        return educationRepository.findAll();
    }
    
}
