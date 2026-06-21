package com.example.ResumeBuilder.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResumeCreateDTO {

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "templateId is required")
    private Long templateId;

    public ResumeCreateDTO() {
    }

    public ResumeCreateDTO(String title, Long userId, Long templateId) {
        this.title = title;
        this.userId = userId;
        this.templateId = templateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}