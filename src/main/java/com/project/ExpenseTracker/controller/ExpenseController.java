package com.project.ExpenseTracker.controller;

import com.project.ExpenseTracker.dto.ExpenseRequest;
import com.project.ExpenseTracker.dto.ExpenseResponse;
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
}
