package com.project.ExpenseTracker.controller;

import com.project.ExpenseTracker.dto.ExpenseRequest;
import com.project.ExpenseTracker.dto.ExpenseResponse;
import com.project.ExpenseTracker.dto.MonthlyCategoryReportResponse;
import com.project.ExpenseTracker.dto.MonthlyReportResponse;
import com.project.ExpenseTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> generateExpense(
            @RequestAttribute("authenticatedUserId") Long authenticatedUserId,
            @RequestBody ExpenseRequest expenseRequest
    ) {
        return ResponseEntity.ok(expenseService.generateExpense(authenticatedUserId, expenseRequest));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long expenseId,
            @RequestAttribute("authenticatedUserId") Long authenticatedUserId,
            @RequestBody ExpenseRequest expenseRequest
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseId, authenticatedUserId, expenseRequest));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long expenseId,
            @RequestAttribute("authenticatedUserId") Long authenticatedUserId
    ) {
        expenseService.deleteExpense(expenseId, authenticatedUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserId(
            @RequestAttribute("authenticatedUserId") Long authenticatedUserId
    ) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(authenticatedUserId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponse>> filterExpenses(
            @RequestAttribute("authenticatedUserId") Long authenticatedUserId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return ResponseEntity.ok(
                expenseService.getExpensesByDateRange(authenticatedUserId, startDate, endDate)
        );
    }

    
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyExpense(
            @RequestAttribute("authenticatedUserId") Long authenticatedUserId,
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(
                expenseService.getMonthlyExpense(authenticatedUserId, month, year)
        );
    }

    @GetMapping("/monthly/byCategory")
    public ResponseEntity<MonthlyCategoryReportResponse> getMonthlyExpenseByCategory(
            @RequestAttribute("authenticatedUserId") Long authenticatedUserId,
            @RequestParam String category,
            @RequestParam int month,
            @RequestParam int year
    ){
        return ResponseEntity.ok(expenseService.getMonthlyExpenseByCategory(authenticatedUserId,category,month,year));
    }

}
