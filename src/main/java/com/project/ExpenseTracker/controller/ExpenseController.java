package com.project.ExpenseTracker.controller;

import com.project.ExpenseTracker.dto.ExpenseRequest;
import com.project.ExpenseTracker.dto.ExpenseResponse;
import com.project.ExpenseTracker.dto.MonthlyReportResponse;
import com.project.ExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserId(@RequestHeader(value = "X-User-ID") Long id){
        return ResponseEntity.ok(expenseService.getExpensesByUserId(id));
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
    public ResponseEntity<MonthlyReportResponse>getMonthlyExpenseByCategory(
            @RequestParam Long userId,
            @RequestParam String category,
            @RequestParam int month,
            @RequestParam int year
    ){
        return ResponseEntity.ok(expenseService.getMonthlyExpenseByCategory(userId,category,month,year));
    }

}
