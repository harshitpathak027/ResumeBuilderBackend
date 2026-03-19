package com.example.ResumeBuilder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class Personal {

	@Id
	@Column(name = "resume_id")
	private Long resumeId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "resume_id")
	private Resume resume;

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "last_name", length = 50)
	private String lastName;

	@Column(length = 100)
	private String email;

	@Column(length = 20)
	private String phone;

	@Column(length = 100)
	private String location;

	@Column(name = "linkedin_url", length = 255)
	private String linkedinUrl;

	@Column(name = "website_url", length = 255)
	private String websiteUrl;

	@Column(name = "professional_summary", columnDefinition = "TEXT")
	private String professionalSummary;

	public Personal() {
	}

	public Personal(Long resumeId, Resume resume, String firstName, String lastName, String email, String phone,
			String location, String linkedinUrl, String websiteUrl, String professionalSummary) {
		this.resumeId = resumeId;
		this.resume = resume;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.location = location;
		this.linkedinUrl = linkedinUrl;
		this.websiteUrl = websiteUrl;
		this.professionalSummary = professionalSummary;
	}

	public Long getResumeId() {
		return resumeId;
	}

	public void setResumeId(Long resumeId) {
		this.resumeId = resumeId;
	}

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLinkedinUrl() {
		return linkedinUrl;
	}

	public void setLinkedinUrl(String linkedinUrl) {
		this.linkedinUrl = linkedinUrl;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getProfessionalSummary() {
		return professionalSummary;
	}

	public void setProfessionalSummary(String professionalSummary) {
		this.professionalSummary = professionalSummary;
	}
}
