package com.project.ExpenseTracker.service;

import com.project.ExpenseTracker.dto.DashboardResponse;
import com.project.ExpenseTracker.model.Category;
import com.project.ExpenseTracker.model.Expense;
import com.project.ExpenseTracker.repository.ExpenseRepository;
import com.project.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
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

        YearMonth currentMonth = YearMonth.now();
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Expense> expenseList = expenseRepository.findByUserIdAndDateBetween(userId, monthStart, monthEnd);
        List<Expense> allExpenseList = expenseRepository.findByUserId(userId);

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

        double averageDailyExpense = 0.0;
        long totalSpendingDays = expenseList.stream()
                .map(expense -> expense.getDate().toLocalDate())
                .distinct()
                .count();

        if (totalSpendingDays > 0) {
            double rawAverage = totalSpent / totalSpendingDays;
            averageDailyExpense = BigDecimal.valueOf(rawAverage)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        Map<Category, Double> categoryMapTotal = allExpenseList.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.summingDouble(Expense::getAmount),
                                total -> BigDecimal.valueOf(total)
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .doubleValue()
                        )
                ));

        Map<Category, Double> currentMonthCategoryMapTotal = expenseList.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.summingDouble(Expense::getAmount),
                                total -> BigDecimal.valueOf(total)
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .doubleValue()
                        )
                ));

        return new DashboardResponse(
                userId,
                totalSpent,
                highestCategory,
                averageDailyExpense,
                categoryMapTotal,
                currentMonthCategoryMapTotal
        );
    }
}
