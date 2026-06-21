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

import com.example.ResumeBuilder.model.Project;
import com.example.ResumeBuilder.service.ProjectService;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project data) {
        return ResponseEntity.status(201).body(projectService.addProject(data));
    }

    @GetMapping("/projects/resume/{resumeId}")
    public ResponseEntity<List<Project>> getProjectsByResumeId(@PathVariable Long resumeId) {
        return ResponseEntity.ok(projectService.getProjectsByResumeId(resumeId));
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody Project data) {
        return ResponseEntity.ok(projectService.updateProject(id, data));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
