package com.example.ResumeBuilder.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.model.Skill;
import com.example.ResumeBuilder.repository.ResumeRepository;
import com.example.ResumeBuilder.repository.SkillRepository;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    public Skill create(Skill skill) {
        Resume resume = resolveResume(skill);
        validate(skill);
        skill.setResume(resume);
        if (skill.getRating() == null) skill.setRating(1);
        if (skill.getSortOrder() == null) skill.setSortOrder(0);
        return skillRepository.save(skill);
    }

    public List<Skill> findByResume(Long resumeId) {
        return skillRepository.findByResumeId(resumeId);
    }

    public Skill update(UUID id, Skill payload) {
        validate(payload);
        Skill current = skillRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Skill not found"));
        current.setSkillName(payload.getSkillName());
        current.setCategory(payload.getCategory());
        current.setRating(payload.getRating());
        current.setSortOrder(payload.getSortOrder());
        return skillRepository.save(current);
    }

    public void delete(UUID id) {
        skillRepository.deleteById(id);
    }

    private Resume resolveResume(Skill skill) {
        if (skill == null || skill.getResume() == null || skill.getResume().getId() == null) throw new IllegalArgumentException("resume.id is required");
        return resumeRepository.findById(skill.getResume().getId()).orElseThrow(() -> new IllegalArgumentException("Resume not found"));
    }

    private void validate(Skill skill) {
        if (skill == null || skill.getSkillName() == null || skill.getSkillName().trim().isEmpty()) throw new IllegalArgumentException("skillName is required");
        if (skill.getCategory() == null || skill.getCategory().trim().isEmpty()) skill.setCategory("Other");
    }
}
