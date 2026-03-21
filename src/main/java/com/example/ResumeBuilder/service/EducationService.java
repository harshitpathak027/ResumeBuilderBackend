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
        validateEducation(education);
        return educationRepository.save(education);
    }

    public List<Education>  getEducation() {
        return educationRepository.findAll();
    }

    public Education getEducationById(UUID id) {
        return educationRepository.findById((id)).orElse(null);
    }

    public Education updateEducation(UUID id, Education payload) {
        validateEducation(payload);

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

    private void validateEducation(Education payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Education payload is required");
        }

        if (payload.getResume() == null || payload.getResume().getId() == null) {
            throw new IllegalArgumentException("resume.id is required");
        }

        if (isBlank(payload.getSchool()) || isBlank(payload.getDegree()) || isBlank(payload.getField())
                || isBlank(payload.getLocation()) || isBlank(payload.getStartDate()) || isBlank(payload.getEndDate())
                || isBlank(payload.getGpa()) || isBlank(payload.getAchievements())) {
            throw new IllegalArgumentException(
                    "school, degree, field, location, startDate, endDate, gpa and achievements are required");
        }

        if (payload.getIsCurrent() == null) {
            throw new IllegalArgumentException("isCurrent is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    
}
