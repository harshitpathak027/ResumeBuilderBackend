package com.example.ResumeBuilder.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.model.WorkExperience;
import com.example.ResumeBuilder.repository.WorkExperienceRepository;

@Service
public class WorkExperienceService {

    @Autowired
    WorkExperienceRepository workExperienceRepository;

    public WorkExperience addWorkExperience(WorkExperience workExperience) {
        validateWorkExperience(workExperience);
        if (workExperience.getSortOrder() == null) {
            workExperience.setSortOrder(0);
        }
        return workExperienceRepository.save(workExperience);
    }

    public List<WorkExperience> getWorkExperienceByResumeId(Long resumeId) {
        return workExperienceRepository.findByResumeId(resumeId);
    }

    public WorkExperience getWorkExperienceById(UUID id) {
        return workExperienceRepository.findById(id).orElse(null);
    }

    public WorkExperience updateWorkExperience(UUID id, WorkExperience payload) {
        validateWorkExperience(payload);

        WorkExperience existing = workExperienceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Work experience not found: " + id));

        existing.setResume(payload.getResume());
        existing.setJobTitle(payload.getJobTitle());
        existing.setCompany(payload.getCompany());
        existing.setLocation(payload.getLocation());
        existing.setStartDate(payload.getStartDate());
        existing.setEndDate(payload.getEndDate());
        existing.setIsCurrent(payload.getIsCurrent());
        existing.setDescription(payload.getDescription());
        existing.setSortOrder(payload.getSortOrder() == null ? 0 : payload.getSortOrder());

        return workExperienceRepository.save(existing);
    }

    public void deleteWorkExperience(UUID id) {
        workExperienceRepository.deleteById(id);
    }

    private void validateWorkExperience(WorkExperience payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Work experience payload is required");
        }

        if (payload.getResume() == null || payload.getResume().getId() == null) {
            throw new IllegalArgumentException("resume.id is required");
        }

        if (isBlank(payload.getJobTitle()) || isBlank(payload.getCompany()) || isBlank(payload.getLocation())
                || isBlank(payload.getStartDate()) || isBlank(payload.getEndDate())
                || isBlank(payload.getDescription())) {
            throw new IllegalArgumentException(
                    "jobTitle, company, location, startDate, endDate and description are required");
        }

        if (payload.getIsCurrent() == null) {
            throw new IllegalArgumentException("isCurrent is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
