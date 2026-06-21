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

import com.example.ResumeBuilder.model.Education;
import com.example.ResumeBuilder.service.EducationService;


@RestController
public class EducationController {
    @Autowired
    EducationService educationService;


    @PostMapping("/education")
    public ResponseEntity<Education> createEducation(@RequestBody Education data){ 
        return ResponseEntity.status(201).body(educationService.addEducation(data));
    }

    @GetMapping("/education")
    public ResponseEntity<List<Education>> getEducationByResumeId(){ 
        return ResponseEntity.ok(educationService.getEducation());
    }

    @GetMapping("/education/{id}")
    public ResponseEntity<Education> getEducationById(@PathVariable UUID id){
        return ResponseEntity.ok(educationService.getEducationById(id));
    }

    @PutMapping("/education/{id}")
    public ResponseEntity<Education> updateEducation(@PathVariable UUID id, @RequestBody Education data){
        return ResponseEntity.ok(educationService.updateEducation(id, data));
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable UUID id) {
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }
}
