package com.example.ResumeBuilder.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ResumeBuilder.model.Resume;
import com.example.ResumeBuilder.model.User;
import com.example.ResumeBuilder.repository.ResumeRepository;
import com.example.ResumeBuilder.repository.UserRepository;
import com.example.ResumeBuilder.service.ResumeExportService;

@RestController
public class ResumeExportController {

    @Autowired
    ResumeExportService resumeExportService;

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    UserRepository userRepository;

    private ResponseEntity<?> validateOwnership(Long resumeId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("message", "Unauthorized"));
        }

        Resume resume = resumeRepository.findById(resumeId).orElse(null);
        if (resume == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("message", "Resume not found"));
        }

        String identifier = authentication.getName().trim();
        User currentUser = userRepository.findByNameIgnoreCaseOrEmailIgnoreCase(identifier, identifier);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("message", "User not found"));
        }

        Long ownerId = resume.getUser() != null ? resume.getUser().getId() : null;
        if (ownerId == null || !ownerId.equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(java.util.Map.of("message", "You are not allowed to access this resume"));
        }

        return null;
    }

    @GetMapping(value = "/resumes/{id}/preview", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> previewResume(@PathVariable Long id, Authentication authentication) {
        ResponseEntity<?> ownershipError = validateOwnership(id, authentication);
        if (ownershipError != null) {
            return ownershipError;
        }

        String html = resumeExportService.buildPreviewHtml(id);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping(value = "/resumes/{id}/export-pdf")
    public ResponseEntity<?> exportPdf(@PathVariable Long id, Authentication authentication) {
        ResponseEntity<?> ownershipError = validateOwnership(id, authentication);
        if (ownershipError != null) {
            return ownershipError;
        }

        try {
            byte[] pdfBytes = resumeExportService.buildPdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("resume-" + id + ".pdf", StandardCharsets.UTF_8)
                    .build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
            } catch (Exception exception) {
                String detail = exception.getMessage();
                if ((detail == null || detail.isBlank()) && exception.getCause() != null) {
                    detail = exception.getCause().getMessage();
                }
                if (exception.getCause() != null && exception.getCause().getMessage() != null
                        && !exception.getCause().getMessage().isBlank()) {
                    detail = exception.getCause().getMessage();
                }
                if (detail == null || detail.isBlank()) {
                    detail = "Unknown PDF generation error";
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                        .body(java.util.Map.of("error", "Failed to generate PDF", "detail", detail));
        }
    }
}
