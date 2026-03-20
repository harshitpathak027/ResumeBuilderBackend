package com.example.ResumeBuilder.service;

import java.util.List;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.DTO.TemplateDTO;
import com.example.ResumeBuilder.model.Template;
import com.example.ResumeBuilder.repository.TemplateRepository;

@Service
public class TemplateService {
    
    @Autowired
    TemplateRepository templateRepository;

    public String createTemplate(TemplateDTO templateDTO){
        Template template = new Template();
        template.setName(templateDTO.getName());
        template.setDescription(templateDTO.getDescription());
        template.setFilePath(templateDTO.getFilePath());
        templateRepository.save(template);
        return "Template created successfully";
    }

    public List<Template> getAllTemplate() {
        List<Template> templates = templateRepository.findAll();
        templates.sort(
            Comparator.comparing(
                    Template::getName,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                )
                .thenComparing(
                    Template::getDescription,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                )
        );
        return templates;
    }
}
