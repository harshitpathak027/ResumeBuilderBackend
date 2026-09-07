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

import com.example.ResumeBuilder.model.Skill;
import com.example.ResumeBuilder.service.SkillService;

@RestController
public class SkillController {
    @Autowired
    private SkillService skillService;

    @PostMapping("/skills")
    public ResponseEntity<Skill> create(@RequestBody Skill skill) {
        return ResponseEntity.status(201).body(skillService.create(skill));
    }

    @GetMapping("/skills/resume/{resumeId}")
    public ResponseEntity<List<Skill>> findByResume(@PathVariable Long resumeId) {
        return ResponseEntity.ok(skillService.findByResume(resumeId));
    }

    @PutMapping("/skills/{id}")
    public ResponseEntity<Skill> update(@PathVariable UUID id, @RequestBody Skill skill) {
        return ResponseEntity.ok(skillService.update(id, skill));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        skillService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
