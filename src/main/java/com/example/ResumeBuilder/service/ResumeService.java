package com.example.ResumeBuilder.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ResumeBuilder.DTO.ResumeCreateDTO;
import com.example.ResumeBuilder.DTO.ResumeResponseDTO;
import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.model.Template;
import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.ResumeRepository;
import com.example.ResumeBuilder.repository.EducationRepository;
import com.example.ResumeBuilder.repository.PersonalRepository;
import com.example.ResumeBuilder.repository.ProjectRepository;
import com.example.ResumeBuilder.repository.ProjectTechRepository;
import com.example.ResumeBuilder.repository.SkillRepository;
import com.example.ResumeBuilder.repository.TemplateRepository;
import com.example.ResumeBuilder.repository.UserRepository;
import com.example.ResumeBuilder.repository.WorkExperienceRepository;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectTechRepository projectTechRepository;

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
        List<Resume>listResume =  resumeRepository.findByUserId(userid);
        listResume.sort(Comparator.comparing(Resume::getUpdatedAt).reversed());
        return listResume;
    }

    @Transactional
    public void deleteResume(Long resumeId, Long userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found"));
        if (resume.getUser() == null || !resume.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Resume does not belong to this user");
        }

        projectRepository.findByResumeId(resumeId).forEach(project -> {
            if (project.getId() != null) projectTechRepository.deleteByProjectId(project.getId());
        });
        projectRepository.deleteByResumeId(resumeId);
        educationRepository.deleteByResumeId(resumeId);
        workExperienceRepository.deleteByResumeId(resumeId);
        skillRepository.deleteByResumeId(resumeId);
        personalRepository.deleteByResumeId(resumeId);
        resumeRepository.delete(resume);
    }
}