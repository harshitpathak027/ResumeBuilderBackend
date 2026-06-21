package com.example.ResumeBuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.model.Personal;
import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.repository.PersonalRepository;
import com.example.ResumeBuilder.repository.ResumeRepository;

@Service
public class PersonalService {

    @Autowired
    PersonalRepository personalRepository;

    @Autowired
    ResumeRepository resumeRepository;
    
    public Personal createPersonal(Personal person) {
        return upsertByResumeId(person.getResumeId(), person);
    }
    
    public Personal getPersonalById(Long id) {
        return personalRepository.findById(id).orElse(null);
    }
    
    public Personal updatePersonal(Personal person) {
        return upsertByResumeId(person.getResumeId(), person);
    }

    private Personal upsertByResumeId(Long resumeId, Personal payload) {
        if (payload == null || resumeId == null) {
            throw new IllegalArgumentException("resumeId is required");
        }

        if (isBlank(payload.getFirstName()) || isBlank(payload.getLastName()) || isBlank(payload.getEmail())) {
            throw new IllegalArgumentException("firstName, lastName and email are required");
        }

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found for id: " + resumeId));

        Personal target = personalRepository.findById(resumeId).orElseGet(Personal::new);
        target.setResume(resume);
        if (target.getResumeId() != null) {
            target.setResumeId(resumeId);
        }
        target.setFirstName(payload.getFirstName());
        target.setLastName(payload.getLastName());
        target.setEmail(payload.getEmail());
        target.setPhone(payload.getPhone());
        target.setLocation(payload.getLocation());
        target.setLinkedinUrl(payload.getLinkedinUrl());
        target.setWebsiteUrl(payload.getWebsiteUrl());
        target.setProfessionalSummary(payload.getProfessionalSummary());

        return personalRepository.save(target);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    
}

