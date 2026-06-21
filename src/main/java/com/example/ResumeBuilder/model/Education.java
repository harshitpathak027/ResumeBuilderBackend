package com.example.ResumeBuilder.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(length = 100, nullable = false)
    private String school;

    @Column(length = 100, nullable = false)
    private String degree;

    @Column(length = 100, nullable = false)
    private String field;

    @Column(length = 100, nullable = false)
    private String location;

    @Column(name = "start_date", length = 20, nullable = false)
    private String startDate;

    @Column(name = "end_date", length = 20, nullable = false)
    private String endDate;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent;

    @Column(length = 10, nullable = false)
    private String gpa;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String achievements;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public Education() {
    }

    public Education(UUID id, Resume resume, String school, String degree, String field, String location,
            String startDate, String endDate, Boolean isCurrent, String gpa, String achievements, Integer sortOrder) {
        this.id = id;
        this.resume = resume;
        this.school = school;
        this.degree = degree;
        this.field = field;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent;
        this.gpa = gpa;
        this.achievements = achievements;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}