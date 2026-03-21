package com.example.ResumeBuilder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ResumeBuilder.DTO.ResumeCreateDTO;
import com.example.ResumeBuilder.DTO.ResumeResponseDTO;
import com.example.ResumeBuilder.service.ResumeService;

@RestController
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/resumes")
    public ResponseEntity<?> createResume(@Valid @RequestBody ResumeCreateDTO resumeCreateDTO) {
        try {
            ResumeResponseDTO resumeResponseDTO = resumeService.createResume(resumeCreateDTO);
            return ResponseEntity.status(201).body(resumeResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/resumes/user/{userid}")
    public ResponseEntity<?> getResumeByUserId(@PathVariable Long userid) {
            return ResponseEntity.status(200).body(resumeService.getResumeByUserId(userid));
    }
}