package com.project.ExpenseTracker.service;

import com.project.ExpenseTracker.dto.ExpenseRequest;
import com.project.ExpenseTracker.dto.ExpenseResponse;
import com.project.ExpenseTracker.dto.MonthlyCategoryReportResponse;
import com.project.ExpenseTracker.dto.MonthlyReportResponse;
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


    public MonthlyReportResponse getMonthlyExpense(Long userId, int month, int year) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<Expense> expenseList = expenseRepository.findByUserId(userId);


        double total = expenseList.stream()
                .filter(expense ->
                        expense.getDate().getMonthValue() == month &&
                                expense.getDate().getYear() == year
                )
                .mapToDouble(Expense::getAmount)
                .sum();

        return new MonthlyReportResponse(userId, month, year, total);
    }

    public MonthlyCategoryReportResponse getMonthlyExpenseByCategory(Long userId, String category, int month, int year) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }

        List<Expense> expenseList = expenseRepository.findByUserId(userId);


        double total = expenseList.stream()
                .filter(expense ->
                        expense.getDate().getMonthValue() == month &&
                                expense.getDate().getYear() == year &&
                                expense.getCategory().name().equalsIgnoreCase(category)
                )
                .mapToDouble(Expense::getAmount)
                .sum();

        return new MonthlyCategoryReportResponse(userId, category, month, year, total);
    }
}
