package com.example.advisingportal.service;

import com.example.advisingportal.entity.AdvisingRequest;
import com.example.advisingportal.entity.SupportCategory;
import com.example.advisingportal.repository.AdvisingRequestRepository;
import com.example.advisingportal.repository.SupportCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvisingService {

    private final AdvisingRequestRepository requestRepo;
    private final SupportCategoryRepository categoryRepo;

    public AdvisingService(AdvisingRequestRepository requestRepo, SupportCategoryRepository categoryRepo) {
        this.requestRepo = requestRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<SupportCategory> getAllCategories() {
        return categoryRepo.findAll();
    }

    public List<AdvisingRequest> getAllRequests() {
        return requestRepo.findAll();
    }

    public List<AdvisingRequest> getRequestsByCategory(Long categoryId) {
        return requestRepo.findByCategoryId(categoryId);
    }

    // CREATE: Enforces the Domain Rule
    public AdvisingRequest createRequest(AdvisingRequest request) {
        if (request.getUrgencyScore() == null || request.getUrgencyScore() < 1 || request.getUrgencyScore() > 3) {
            throw new IllegalArgumentException("Urgency score must be 1, 2, or 3.");
        }
        request.setStatus("SUBMITTED"); // All new requests start here
        return requestRepo.save(request);
    }

    // UPDATE: Enforces the Status Transition Rule
    public AdvisingRequest updateStatus(Long id, String newStatus) {
        AdvisingRequest request = requestRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        String currentStatus = request.getStatus();

        // Enforce transition logic: SUBMITTED -> IN_REVIEW -> RESOLVED
        if (currentStatus.equals("SUBMITTED") && !newStatus.equals("IN_REVIEW")) {
            throw new IllegalStateException("A submitted request must move to IN_REVIEW next.");
        }
        if (currentStatus.equals("IN_REVIEW") && !newStatus.equals("RESOLVED")) {
            throw new IllegalStateException("An in-review request must move to RESOLVED next.");
        }
        if (currentStatus.equals("RESOLVED")) {
            throw new IllegalStateException("Cannot change the status of a resolved request.");
        }

        request.setStatus(newStatus);
        return requestRepo.save(request);
    }

    // DELETE
    public void deleteRequest(Long id) {
        requestRepo.deleteById(id);
    }

    // --- DASHBOARD METHODS ---
    public long getTotalRequests() {
        return requestRepo.count(); // Built-in JPA method for KPI 1
    }

    public long getSubmittedRequestsCount() {
        return requestRepo.countByStatus("SUBMITTED"); // KPI 2
    }

    public List<Object[]> getCategorySummary() {
        return requestRepo.countRequestsByCategory(); // Group-by Table
    }
}