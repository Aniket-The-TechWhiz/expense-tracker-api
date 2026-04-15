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
    public ResponseEntity<ExpenseResponse> generateExpense(@RequestBody ExpenseRequest expenseRequest){
        return ResponseEntity.ok(expenseService.generateExpense(expenseRequest));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long expenseId,
            @RequestHeader(value = "X-User-ID") Long userId,
            @RequestBody ExpenseRequest expenseRequest
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(expenseId, userId, expenseRequest));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long expenseId,
            @RequestHeader(value = "X-User-ID") Long userId
    ) {
        expenseService.deleteExpense(expenseId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserId(@RequestHeader(value = "X-User-ID") Long id){
        return ResponseEntity.ok(expenseService.getExpensesByUserId(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponse>> filterExpenses(
            @RequestParam Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return ResponseEntity.ok(
                expenseService.getExpensesByDateRange(userId, startDate, endDate)
        );
    }

    
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyExpense(
            @RequestParam Long userId,
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(
                expenseService.getMonthlyExpense(userId, month, year)
        );
    }

    @GetMapping("/monthly/byCategory")
    public ResponseEntity<MonthlyCategoryReportResponse> getMonthlyExpenseByCategory(
            @RequestParam Long userId,
            @RequestParam String category,
            @RequestParam int month,
            @RequestParam int year
    ){
        return ResponseEntity.ok(expenseService.getMonthlyExpenseByCategory(userId,category,month,year));
    }

}
