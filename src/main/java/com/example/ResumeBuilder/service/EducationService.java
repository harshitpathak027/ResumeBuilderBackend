package com.example.ResumeBuilder.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.model.Education;
import com.example.ResumeBuilder.repository.EducationRepository;


@Service
public class EducationService {
    @Autowired
    EducationRepository educationRepository;

    public Education addEducation(Education education) {
        return educationRepository.save(education);
    }

    public List<Education>  getEducation() {
        return educationRepository.findAll();
    }

    public Education getEducationById(UUID id) {
        return educationRepository.findById((id)).orElse(null);
    }

    public Education updateEducation(UUID id, Education payload) {
        Education existing = educationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Education not found: " + id));

        existing.setSchool(payload.getSchool());
        existing.setDegree(payload.getDegree());
        existing.setField(payload.getField());
        existing.setLocation(payload.getLocation());
        existing.setStartDate(payload.getStartDate());
        existing.setEndDate(payload.getEndDate());
        existing.setIsCurrent(payload.getIsCurrent());
        existing.setGpa(payload.getGpa());
        existing.setAchievements(payload.getAchievements());
        existing.setSortOrder(payload.getSortOrder());

        if (payload.getResume() != null) {
            existing.setResume(payload.getResume());
        }

        return educationRepository.save(existing);
    }

    public void deleteEducation(UUID id) {
        if (!educationRepository.existsById(id)) {
            throw new IllegalArgumentException("Education not found: " + id);
        }
        educationRepository.deleteById(id);
    }
    
}
