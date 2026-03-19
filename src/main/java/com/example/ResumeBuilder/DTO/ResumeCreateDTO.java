package com.example.ResumeBuilder.DTO;

public class ResumeCreateDTO {

    private String title;
    private Long userId;
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