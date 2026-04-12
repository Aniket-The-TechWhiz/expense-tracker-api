package com.project.ExpenseTracker.service;

import com.project.ExpenseTracker.dto.ExpenseRequest;
import com.project.ExpenseTracker.dto.ExpenseResponse;
import com.project.ExpenseTracker.model.Expense;
import com.project.ExpenseTracker.model.User;
import com.project.ExpenseTracker.repository.ExpenseRepository;
import com.project.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    public ExpenseResponse generateExpense(ExpenseRequest request) {
        User user=userRepository.findById(request.getUserId()).orElseThrow(()->new RuntimeException("Invalid user id"+request.getUserId()));
        Expense expense=Expense.builder()
                .user(user)
                .amount(request.getAmount())
                .category(request.getCategory())
                .build();
        Expense savedExpense=expenseRepository.save(expense);
        return mapToResponse(savedExpense);
    }

    private ExpenseResponse mapToResponse(Expense savedExpense) {
        ExpenseResponse expenseResponse=new ExpenseResponse();
        expenseResponse.setId(savedExpense.getId());
        expenseResponse.setUserId(savedExpense.getUser().getId());
        expenseResponse.setAmount(savedExpense.getAmount());
        expenseResponse.setCategory(savedExpense.getCategory());
        expenseResponse.setDate(savedExpense.getDate());
        return expenseResponse;
    }

    public List<ExpenseResponse> getExpensesByUserId(Long id) {
        List<Expense> expenseList = expenseRepository.findByUserId(id);
        return expenseList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
