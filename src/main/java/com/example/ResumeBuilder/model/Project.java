package com.example.ResumeBuilder.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resume_projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "project_name", length = 100, nullable = false)
    private String projectName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "start_date", length = 20, nullable = false)
    private String startDate;

    @Column(name = "end_date", length = 20, nullable = false)
    private String endDate;

    @Column(name = "live_url", length = 255, nullable = false)
    private String liveUrl;

    @Column(name = "repo_url", length = 255, nullable = false)
    private String repoUrl;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public Project() {
    }

    public Project(UUID id, Resume resume, String projectName, String description, String startDate, String endDate,
            String liveUrl, String repoUrl, Integer sortOrder) {
        this.id = id;
        this.resume = resume;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.liveUrl = liveUrl;
        this.repoUrl = repoUrl;
        this.sortOrder = sortOrder;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}