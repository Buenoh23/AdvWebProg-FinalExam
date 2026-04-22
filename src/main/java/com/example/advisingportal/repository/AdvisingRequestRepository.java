package com.example.advisingportal.repository;

import com.example.advisingportal.entity.AdvisingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <-- This is the import you needed!
import java.util.List;

public interface AdvisingRequestRepository extends JpaRepository<AdvisingRequest, Long> {
    
    // Used for HTMX filtering
    List<AdvisingRequest> findByCategoryId(Long categoryId);

    // KPI 1: Count by a specific status
    long countByStatus(String status);

    // Group-By Table: Counts how many requests belong to each category
    @Query("SELECT r.category.name, COUNT(r) FROM AdvisingRequest r GROUP BY r.category.name")
    List<Object[]> countRequestsByCategory();
}