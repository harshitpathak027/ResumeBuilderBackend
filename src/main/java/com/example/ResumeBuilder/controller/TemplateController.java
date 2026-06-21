package com.example.ResumeBuilder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ResumeBuilder.DTO.TemplateDTO;
import com.example.ResumeBuilder.model.Template;
import com.example.ResumeBuilder.service.TemplateService;

@RestController("templateController")
public class TemplateController {
    
    @Autowired
    TemplateService templateService;


    @PostMapping("/templates")
    public ResponseEntity<String> createTemplate(@RequestBody TemplateDTO templateDTO){ 
        return ResponseEntity.status(201).body(templateService.createTemplate(templateDTO));
    }
    @GetMapping("/templates")
    public ResponseEntity<List<Template>> getAllTemplate(){ 
        return ResponseEntity.ok(templateService.getAllTemplate());
    }

}
