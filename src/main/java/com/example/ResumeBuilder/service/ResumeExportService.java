package com.example.ResumeBuilder.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ResumeBuilder.model.Education;
import com.example.ResumeBuilder.model.Personal;
import com.example.ResumeBuilder.model.Project;
import com.example.ResumeBuilder.model.Skill;
import com.example.ResumeBuilder.model.WorkExperience;
import com.example.ResumeBuilder.repository.EducationRepository;
import com.example.ResumeBuilder.repository.PersonalRepository;
import com.example.ResumeBuilder.repository.ProjectRepository;
import com.example.ResumeBuilder.repository.ResumeRepository;
import com.example.ResumeBuilder.repository.SkillRepository;
import com.example.ResumeBuilder.repository.WorkExperienceRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class ResumeExportService {

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    PersonalRepository personalRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    WorkExperienceRepository workExperienceRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SkillRepository skillRepository;

    public String buildPreviewHtml(Long resumeId) {
        return buildPreviewHtml(resumeId, false);
    }

    private String buildPreviewHtml(Long resumeId, boolean forPdf) {
        resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));

        Personal personal = personalRepository.findById(resumeId).orElse(null);
        List<Education> educations = safeSortEducation(educationRepository.findByResumeId(resumeId));
        List<WorkExperience> experiences = safeSortWork(workExperienceRepository.findByResumeId(resumeId));
        List<Project> projects = safeSortProjects(projectRepository.findByResumeId(resumeId));
        List<Skill> skills = safeSortSkills(skillRepository.findByResumeId(resumeId));

        String fullName = buildName(personal);
        String contactLine = buildContactLine(personal);
        String exportUrl = "/resumes/" + resumeId + "/preview?print=true";

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'/>")
            .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'/>");
        html.append("<style>")
            .append("@page{size:Letter;margin:0.55in 0.6in;}")
            .append("body{font-family:'Times New Roman',Times,serif;background:#f3f4f6;margin:0;color:#111827;line-height:1.25;font-size:11pt;}")
            .append(".viewer{min-height:100vh;overflow-y:auto;-webkit-overflow-scrolling:touch;padding:12px;box-sizing:border-box;}")
            .append(".action-bar{max-width:860px;margin:0 auto 10px;display:flex;justify-content:space-between;gap:10px;position:sticky;top:8px;z-index:12;padding:2px 0;}")
            .append(".action-btn{display:inline-flex;align-items:center;justify-content:center;border:none;background:#e5e7eb;color:#111827;padding:10px 14px;border-radius:14px;font-size:14px;font-family:Arial,Helvetica,sans-serif;font-weight:700;cursor:pointer;text-decoration:none;box-shadow:0 1px 2px rgba(0,0,0,0.08);}")
            .append(".action-btn.primary{background:#0073D5;color:#ffffff;}")
            .append(".page{max-width:860px;margin:4px auto;background:#fff;padding:22px 28px 24px;border:1px solid #d1d5db;box-sizing:border-box;}")
            .append("h1{margin:0;font-size:49px;line-height:1;text-align:center;font-variant:small-caps;letter-spacing:0.8px;font-weight:500;}")
            .append(".contact{margin-top:2px;text-align:center;font-size:14px;line-height:1.25;word-break:break-word;}")
            .append(".section{margin-top:8px;}")
            .append(".section h2{font-size:21px;letter-spacing:0.1px;margin:0 0 4px 0;padding-bottom:2px;border-bottom:1px solid #111827;font-weight:700;}")
            .append(".entry{margin:4px 0 7px 0;page-break-inside:avoid;}")
            .append(".row{display:flex;justify-content:space-between;gap:12px;align-items:flex-start;}")
            .append(".title{font-weight:700;font-size:13px;line-height:1.2;}")
            .append(".right{font-weight:700;font-size:13px;white-space:nowrap;line-height:1.2;}")
            .append(".subtitle{font-style:italic;font-size:12px;line-height:1.24;margin-top:1px;}")
            .append("ul{margin:2px 0 0 24px;padding:0;}")
            .append("li{margin:0 0 1px 0;font-size:12px;line-height:1.24;}")
            .append("p{margin:2px 0;font-size:12px;line-height:1.24;}")
            .append(".skills-line strong{font-weight:700;}")
            .append("@media (max-width:768px){")
            .append("body{background:#ffffff;}")
            .append(".viewer{padding:0;}")
            .append(".action-bar{max-width:none;padding:10px 12px;margin:0;top:0;background:#ffffffcc;backdrop-filter:blur(4px);border-bottom:1px solid #e5e7eb;}")
            .append(".action-btn{font-size:13px;padding:9px 12px;border-radius:12px;}")
            .append(".page{max-width:none;margin:0;border:none;padding:14px 14px 18px;}")
            .append("h1{font-size:24px;letter-spacing:0.4px;}")
            .append(".contact{font-size:13px;line-height:1.35;}")
            .append(".section{margin-top:10px;}")
            .append(".section h2{font-size:13px;padding-bottom:3px;margin-bottom:4px;}")
            .append(".entry{margin:2px 0 7px 0;}")
            .append(".title{font-size:12px;}")
            .append(".right{font-size:12px;}")
            .append(".subtitle{font-size:11px;}")
            .append("ul{margin:2px 0 0 16px;}")
            .append("li{font-size:11px;line-height:1.3;}")
            .append("p{font-size:11px;line-height:1.3;}")
            .append("}")
            .append("@media print{body{background:#fff;} .viewer{height:auto;overflow:visible;padding:0;} .action-bar{display:none;} .page{border:none; margin:0; max-width:none; padding:0;}}")
            .append("</style>");

        if (!forPdf) {
            html.append("<script>")
                .append("window.addEventListener('load', function(){")
                .append("var p = new URLSearchParams(window.location.search);")
                .append("if(p.get('print') === 'true'){ setTimeout(function(){ window.print(); }, 200); }")
                .append("});")
                .append("</script>");
        }

        html.append("</head><body><div class='viewer'>");

        if (!forPdf) {
            html.append("<div class='action-bar'>")
                .append("<button class='action-btn' onclick='window.history.back()'>← Back</button>")
                .append("<a class='action-btn primary' href='").append(exportUrl).append("'>Export PDF</a>")
                .append("</div>");
        }

        html.append("<div class='page'>");

        html.append("<h1>").append(escape(fullName)).append("</h1>");
        html.append("<div class='contact'>").append(escape(contactLine)).append("</div>");

        if (personal != null && notBlank(personal.getProfessionalSummary())) {
            html.append("<div class='section'><h2>Summary</h2><p>")
                .append(escape(personal.getProfessionalSummary()))
                .append("</p></div>");
        }

        html.append("<div class='section'><h2>Education</h2>");
        if (educations.isEmpty()) {
            html.append("<p>No education added yet.</p>");
        } else {
            for (Education education : educations) {
                html.append("<div class='entry'>")
                    .append("<div class='row'><div class='title'>").append(escape(education.getSchool())).append("</div>")
                    .append("<div class='right'>").append(escape(buildDateRange(education.getStartDate(), education.getEndDate(), education.getIsCurrent()))).append("</div></div>")
                    .append("<div class='row'><div class='subtitle'>").append(escape(education.getDegree())).append(" in ")
                    .append(escape(education.getField())).append("</div>")
                    .append("<div class='subtitle'>").append(escape(education.getLocation())).append("</div></div>");

                List<String> eduBullets = new ArrayList<>();
                if (notBlank(education.getAchievements())) {
                    eduBullets.add(education.getAchievements());
                }
                if (notBlank(education.getGpa())) {
                    eduBullets.add("GPA: " + education.getGpa());
                }
                appendBullets(html, eduBullets);
                html.append("</div>");
            }
        }
        html.append("</div>");

        html.append("<div class='section'><h2>Experience</h2>");
        if (experiences.isEmpty()) {
            html.append("<p>No experience added yet.</p>");
        } else {
            for (WorkExperience experience : experiences) {
                html.append("<div class='entry'>")
                    .append("<div class='row'><div class='title'>").append(escape(experience.getCompany())).append("</div>")
                    .append("<div class='right'>").append(escape(buildDateRange(experience.getStartDate(), experience.getEndDate(), experience.getIsCurrent()))).append("</div></div>")
                    .append("<div class='row'><div class='subtitle'>").append(escape(experience.getJobTitle())).append("</div>")
                    .append("<div class='subtitle'>").append(escape(experience.getLocation())).append("</div></div>");

                appendBullets(html, splitBullets(experience.getDescription()));
                html.append("</div>");
            }
        }
        html.append("</div>");

        html.append("<div class='section'><h2>Projects</h2>");
        if (projects.isEmpty()) {
            html.append("<p>No projects added yet.</p>");
        } else {
            for (Project project : projects) {
                html.append("<div class='entry'>")
                    .append("<div class='row'><div class='title'>").append(escape(project.getProjectName())).append("</div>")
                    .append("<div class='right'>").append(escape(buildDateRange(project.getStartDate(), project.getEndDate(), false))).append("</div></div>");

                if (notBlank(project.getTechnologies())) {
                    html.append("<div class='subtitle'>").append(escape(project.getTechnologies())).append("</div>");
                }
                appendBullets(html, splitBullets(project.getDescription()));

                List<String> links = new ArrayList<>();
                if (notBlank(project.getLiveUrl())) {
                    links.add("Live: " + project.getLiveUrl());
                }
                if (notBlank(project.getRepoUrl())) {
                    links.add("Repo: " + project.getRepoUrl());
                }
                if (!links.isEmpty()) {
                    html.append("<p>").append(escape(String.join("  |  ", links))).append("</p>");
                }

                html.append("</div>");
            }
        }
        html.append("</div>");

        html.append("<div class='section'><h2>Technical Skills</h2>");
        if (skills.isEmpty()) {
            html.append("<p>No skills added yet.</p>");
        } else {
            Map<String, List<Skill>> byCategory = skills.stream()
                    .collect(Collectors.groupingBy(s -> notBlank(s.getCategory()) ? s.getCategory() : "General"));

            for (Map.Entry<String, List<Skill>> entry : byCategory.entrySet()) {
                String skillLine = entry.getValue().stream()
                        .map(skill -> notBlank(skill.getSkillName()) ? skill.getSkillName() : "")
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(", "));

                html.append("<p class='skills-line'><strong>").append(escape(entry.getKey())).append(":</strong> ")
                    .append(escape(skillLine))
                    .append("</p>");
            }
        }
        html.append("</div>");

        html.append("</div></div></body></html>");
        return html.toString();
    }

    public byte[] buildPdf(Long resumeId) {
        try {
            String html = buildPreviewHtml(resumeId, true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, "http://localhost:8080");
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to generate PDF", exception);
        }
    }

    private List<Education> safeSortEducation(List<Education> source) {
        source.sort(Comparator.comparing(Education::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        return source;
    }

    private List<WorkExperience> safeSortWork(List<WorkExperience> source) {
        source.sort(Comparator.comparing(WorkExperience::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        return source;
    }

    private List<Project> safeSortProjects(List<Project> source) {
        source.sort(Comparator.comparing(Project::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        return source;
    }

    private List<Skill> safeSortSkills(List<Skill> source) {
        source.sort(Comparator.comparing(Skill::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        return source;
    }

    private String buildName(Personal personal) {
        if (personal == null) {
            return "First Last";
        }
        String firstName = safe(personal.getFirstName());
        String lastName = safe(personal.getLastName());
        String fullName = (firstName + " " + lastName).trim();
        return fullName.isEmpty() ? "First Last" : fullName;
    }

    private String buildContactLine(Personal personal) {
        if (personal == null) {
            return "email@example.com | +1 000-000-0000 | linkedin.com/in/username | github.com/username";
        }

        List<String> parts = new ArrayList<>();
        if (notBlank(personal.getEmail())) {
            parts.add(personal.getEmail());
        }
        if (notBlank(personal.getPhone())) {
            parts.add(personal.getPhone());
        }
        if (notBlank(personal.getLocation())) {
            parts.add(personal.getLocation());
        }
        if (notBlank(personal.getLinkedinUrl())) {
            parts.add(personal.getLinkedinUrl());
        }
        if (notBlank(personal.getWebsiteUrl())) {
            parts.add(personal.getWebsiteUrl());
        }

        if (parts.isEmpty()) {
            return "email@example.com | +1 000-000-0000";
        }
        return String.join(" | ", parts);
    }

    private String buildDateRange(String startDate, String endDate, Boolean isCurrent) {
        String safeStart = safe(startDate);
        String safeEnd = Boolean.TRUE.equals(isCurrent) ? "Present" : safe(endDate);
        if (safeStart.isEmpty() && safeEnd.isEmpty()) {
            return "";
        }
        if (safeStart.isEmpty()) {
            return safeEnd;
        }
        if (safeEnd.isEmpty()) {
            return safeStart;
        }
        return safeStart + " - " + safeEnd;
    }

    private List<String> splitBullets(String text) {
        if (!notBlank(text)) {
            return List.of();
        }
        String[] parts = text.split("\\r?\\n|•");
        List<String> bullets = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                bullets.add(trimmed.endsWith(".") ? trimmed : trimmed + ".");
            }
        }
        return bullets;
    }

    private void appendBullets(StringBuilder html, List<String> bullets) {
        if (bullets == null || bullets.isEmpty()) {
            return;
        }

        html.append("<ul>");
        for (String bullet : bullets) {
            if (notBlank(bullet)) {
                html.append("<li>").append(escape(bullet)).append("</li>");
            }
        }
        html.append("</ul>");
    }

    private boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
