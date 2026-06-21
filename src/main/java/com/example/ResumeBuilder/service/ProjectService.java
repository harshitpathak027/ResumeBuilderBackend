package com.example.ResumeBuilder.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.model.Project;
import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.repository.ProjectRepository;
import com.example.ResumeBuilder.repository.ResumeRepository;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ResumeRepository resumeRepository;

    public Project addProject(Project project) {
        validateProject(project);
        Resume resume = resolveResume(project);
        project.setResume(resume);

        if (project.getSortOrder() == null) {
            project.setSortOrder(0);
        }
        if (project.getTechnologies() == null) {
            project.setTechnologies("");
        }
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByResumeId(Long resumeId) {
        return projectRepository.findByResumeId(resumeId);
    }

    public Project getProjectById(UUID id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project updateProject(UUID id, Project payload) {
        validateProject(payload);

        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));

        Resume resume = resolveResume(payload);
        existing.setResume(resume);
        existing.setProjectName(payload.getProjectName());
        existing.setDescription(payload.getDescription());
        existing.setStartDate(payload.getStartDate());
        existing.setEndDate(payload.getEndDate());
        existing.setLiveUrl(payload.getLiveUrl());
        existing.setRepoUrl(payload.getRepoUrl());
        existing.setTechnologies(payload.getTechnologies() == null ? "" : payload.getTechnologies());
        existing.setSortOrder(payload.getSortOrder() == null ? 0 : payload.getSortOrder());

        return projectRepository.save(existing);
    }

    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }

    private Resume resolveResume(Project project) {
        if (project == null || project.getResume() == null || project.getResume().getId() == null) {
            throw new IllegalArgumentException("resume.id is required");
        }

        Long resumeId = project.getResume().getId();
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));
    }

    private void validateProject(Project payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Project payload is required");
        }

        if (payload.getResume() == null || payload.getResume().getId() == null) {
            throw new IllegalArgumentException("resume.id is required");
        }

        if (isBlank(payload.getProjectName()) || isBlank(payload.getDescription()) || isBlank(payload.getTechnologies())
                || isBlank(payload.getStartDate()) || isBlank(payload.getEndDate()) || isBlank(payload.getLiveUrl())
                || isBlank(payload.getRepoUrl())) {
            throw new IllegalArgumentException(
                    "projectName, description, technologies, startDate, endDate, liveUrl and repoUrl are required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
