package com.example.ResumeBuilder.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ResumeBuilder.model.WorkExperience;
import com.example.ResumeBuilder.service.WorkExperienceService;

@RestController
public class WorkExperienceController {

    @Autowired
    WorkExperienceService workExperienceService;

    @PostMapping("/work-experience")
    public ResponseEntity<WorkExperience> createWorkExperience(@RequestBody WorkExperience data) {
        return ResponseEntity.status(201).body(workExperienceService.addWorkExperience(data));
    }

    @GetMapping("/work-experience/resume/{resumeId}")
    public ResponseEntity<List<WorkExperience>> getByResumeId(@PathVariable Long resumeId) {
        return ResponseEntity.ok(workExperienceService.getWorkExperienceByResumeId(resumeId));
    }

    @GetMapping("/work-experience/{id}")
    public ResponseEntity<WorkExperience> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(workExperienceService.getWorkExperienceById(id));
    }

    @PutMapping("/work-experience/{id}")
    public ResponseEntity<WorkExperience> updateWorkExperience(@PathVariable UUID id, @RequestBody WorkExperience data) {
        return ResponseEntity.ok(workExperienceService.updateWorkExperience(id, data));
    }

    @DeleteMapping("/work-experience/{id}")
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable UUID id) {
        workExperienceService.deleteWorkExperience(id);
        return ResponseEntity.noContent().build();
    }
}
