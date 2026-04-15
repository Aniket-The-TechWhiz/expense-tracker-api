package com.project.ExpenseTracker.service;

import com.project.ExpenseTracker.dto.DashboardResponse;
import com.project.ExpenseTracker.model.Category;
import com.project.ExpenseTracker.model.Expense;
import com.project.ExpenseTracker.repository.ExpenseRepository;
import com.project.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public DashboardResponse getDashboardSummary(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Expense> expenseList = expenseRepository.findByUserId(userId);

        double totalSpent = expenseList.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Map<Category, Double> categoryTotals = expenseList.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        String highestCategory = categoryTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().name())
                .orElse("N/A");

        Map<LocalDate, Double> dailyTotals = expenseList.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getDate().toLocalDate(),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        double averageDailyExpense = dailyTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new DashboardResponse(userId, totalSpent, highestCategory, averageDailyExpense);
    }
}
