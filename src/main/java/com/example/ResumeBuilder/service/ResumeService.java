package com.example.ResumeBuilder.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.DTO.ResumeCreateDTO;
import com.example.ResumeBuilder.DTO.ResumeResponseDTO;
import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.model.Template;
import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.ResumeRepository;
import com.example.ResumeBuilder.repository.TemplateRepository;
import com.example.ResumeBuilder.repository.UserRepository;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    public ResumeResponseDTO createResume(ResumeCreateDTO resumeCreateDTO) {
        if (resumeCreateDTO.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }

        if (resumeCreateDTO.getTemplateId() == null) {
            throw new IllegalArgumentException("templateId is required");
        }

        User user = userRepository.findById(resumeCreateDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Template template = templateRepository.findById(resumeCreateDTO.getTemplateId())
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        Resume resume = new Resume();
        resume.setTitle(resumeCreateDTO.getTitle());
        resume.setUser(user);
        resume.setTemplate(template);

        Resume savedResume = resumeRepository.save(resume);

        return new ResumeResponseDTO(
                savedResume.getId(),
                savedResume.getTitle(),
                savedResume.getUser() != null ? savedResume.getUser().getId() : null,
                savedResume.getTemplate() != null ? savedResume.getTemplate().getId() : null,
                savedResume.getCreatedAt(),
                savedResume.getUpdatedAt());
    }

    public List<Resume> getResumeByUserId(Long userid) {
        return resumeRepository.findByUserId(userid);
    }
}