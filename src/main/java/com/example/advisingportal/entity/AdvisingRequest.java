package com.example.advisingportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AdvisingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private String status; //SUBMITTED, IN_REVIEW, RESOLVED
    
    private LocalDateTime createdAt;
    
    private Integer urgencyScore; //Number field (1, 2, or 3)

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SupportCategory category;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructors
    public AdvisingRequest() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getUrgencyScore() { return urgencyScore; }
    public void setUrgencyScore(Integer urgencyScore) { this.urgencyScore = urgencyScore; }

    public SupportCategory getCategory() { return category; }
    public void setCategory(SupportCategory category) { this.category = category; }
}