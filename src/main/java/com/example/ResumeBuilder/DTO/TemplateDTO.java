package com.example.ResumeBuilder.DTO;

public class TemplateDTO {

    private Long id;
    private String name;
    private String description;
    private String filePath;

    public TemplateDTO() {
    }

    public TemplateDTO(Long id, String name, String description, String filePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
