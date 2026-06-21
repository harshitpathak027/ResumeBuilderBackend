package com.example.ResumeBuilder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ResumeBuilder.model.Project;
import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.EducationRepository;
import com.example.ResumeBuilder.repository.PersonalRepository;
import com.example.ResumeBuilder.repository.ProjectRepository;
import com.example.ResumeBuilder.repository.ProjectTechRepository;
import com.example.ResumeBuilder.repository.ResumeRepository;
import com.example.ResumeBuilder.repository.SkillRepository;
import com.example.ResumeBuilder.repository.UserRepository;
import com.example.ResumeBuilder.repository.WorkExperienceRepository;

@Service
public class AccountDeletionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

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

    @Transactional
    public boolean deleteAccountByLoginIdentifier(String loginIdentifier) {
        if (loginIdentifier == null || loginIdentifier.trim().isEmpty()) {
            return false;
        }

        String identifier = loginIdentifier.trim();
        User user = userRepository.findByNameIgnoreCaseOrEmailIgnoreCase(identifier, identifier);
        if (user == null || user.getId() == null) {
            return false;
        }

        Long userId = user.getId();
        List<Resume> resumes = resumeRepository.findByUserId(userId);

        for (Resume resume : resumes) {
            if (resume == null || resume.getId() == null) {
                continue;
            }

            Long resumeId = resume.getId();

            // ProjectTech -> Project
            List<Project> projects = projectRepository.findByResumeId(resumeId);
            for (Project project : projects) {
                if (project != null && project.getId() != null) {
                    projectTechRepository.deleteByProjectId(project.getId());
                }
            }
            projectRepository.deleteByResumeId(resumeId);

            // Other resume sections
            educationRepository.deleteByResumeId(resumeId);
            workExperienceRepository.deleteByResumeId(resumeId);
            skillRepository.deleteByResumeId(resumeId);
            personalRepository.deleteByResumeId(resumeId);

            // Finally the resume itself
            resumeRepository.deleteById(resumeId);
        }

        userRepository.deleteById(userId);
        return true;
    }
}
