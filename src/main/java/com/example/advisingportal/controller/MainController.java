package com.example.advisingportal.controller;

import com.example.advisingportal.entity.AdvisingRequest;
import com.example.advisingportal.service.AdvisingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final AdvisingService advisingService;

    public MainController(AdvisingService advisingService) {
        this.advisingService = advisingService;
    }

    @GetMapping("/")
    public String redirectToMain() {
        return "redirect:/main";
    }

    // 1. GET /main - Main page with full layout
    @GetMapping("/main")
    public String getMainPage(Model model) {
        model.addAttribute("requests", advisingService.getAllRequests());
        model.addAttribute("categories", advisingService.getAllCategories());
        model.addAttribute("newRequest", new AdvisingRequest());
        return "main"; // Returns the full HTML page
    }

    // 2. GET /main/fragment/list - HTMX GET for filtering
    @GetMapping("/main/fragment/list")
    public String getFilteredList(@RequestParam(required = false) Long categoryId, Model model) {
        if (categoryId != null && categoryId > 0) {
            model.addAttribute("requests", advisingService.getRequestsByCategory(categoryId));
        } else {
            model.addAttribute("requests", advisingService.getAllRequests());
        }
        return "fragments/list :: requestList"; // Returns ONLY the fragment
    }

    // 3. POST /main - Create a new record via HTMX
    @PostMapping("/main")
    public String createRequest(@ModelAttribute AdvisingRequest newRequest, Model model) {
        try {
            advisingService.createRequest(newRequest);
        } catch (Exception e) {
            // If the urgency score is invalid, send the error back to the UI
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("requests", advisingService.getAllRequests());
        return "fragments/list :: requestList"; // Returns ONLY the fragment
    }

    // 4. PUT /main/{id} - Update status via HTMX
    @PutMapping("/main/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String newStatus, Model model) {
        try {
            advisingService.updateStatus(id, newStatus);
        } catch (Exception e) {
            // If the status transition is invalid, send the error back to the UI
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("requests", advisingService.getAllRequests());
        return "fragments/list :: requestList"; // Returns ONLY the fragment
    }

    // 5. DELETE /main/{id} - Delete record via HTMX
    @DeleteMapping("/main/{id}")
    public String deleteRequest(@PathVariable Long id, Model model) {
        advisingService.deleteRequest(id);
        model.addAttribute("requests", advisingService.getAllRequests());
        return "fragments/list :: requestList"; // Returns ONLY the fragment
    }

    // 6. GET /dashboard - Dashboard summary page
    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        model.addAttribute("totalRequests", advisingService.getTotalRequests());
        model.addAttribute("submittedRequests", advisingService.getSubmittedRequestsCount());
        model.addAttribute("categorySummary", advisingService.getCategorySummary());
        return "dashboard"; // Returns the full HTML dashboard page
    }
}