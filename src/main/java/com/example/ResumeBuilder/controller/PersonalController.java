package com.example.ResumeBuilder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ResumeBuilder.model.Personal;
import com.example.ResumeBuilder.service.PersonalService;

@RestController
public class PersonalController {
    
    @Autowired
    PersonalService personalService;

    @PostMapping("/personal")
    public ResponseEntity<Personal> createPersonal(@RequestBody Personal personal){
        return ResponseEntity.status(201).body(personalService.createPersonal(personal));
    }

    @GetMapping("/personal/{id}")
    public ResponseEntity<Personal> getPersonalById(@PathVariable Long id){
        return ResponseEntity.ok(personalService.getPersonalById(id));
    }

    @PutMapping("/personal/{id}")
    public ResponseEntity<Personal> updatePersonal(@PathVariable Long id, @RequestBody Personal personal){
        personal.setResumeId(id);
        return ResponseEntity.ok(personalService.updatePersonal(personal));
    }

}


