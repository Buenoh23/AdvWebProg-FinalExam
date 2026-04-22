package com.example.advisingportal.repository;

import com.example.advisingportal.entity.AdvisingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvisingRequestRepository extends JpaRepository<AdvisingRequest, Long> {
    // We will need this for the HTMX filtering feature later!
    List<AdvisingRequest> findByCategoryId(Long categoryId);
}