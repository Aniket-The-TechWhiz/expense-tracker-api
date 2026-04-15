package com.project.ExpenseTracker.controller;

import com.project.ExpenseTracker.dto.DashboardResponse;
import com.project.ExpenseTracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardSummary(@RequestParam Long userId) {
        return ResponseEntity.ok(dashboardService.getDashboardSummary(userId));
    }
}
