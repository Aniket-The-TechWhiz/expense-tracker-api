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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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

        double totalSpent = expenseRepository.sumAmountByUserIdAndDateBetween(userId, monthStart, monthEnd);

        Map<Category, Double> currentMonthCategoryMapTotal = mapCategoryTotals(
                expenseRepository.sumAmountByCategoryForDateRange(userId, monthStart, monthEnd)
        );

        Map<Category, Double> categoryMapTotal = mapCategoryTotals(
                expenseRepository.sumAmountByCategoryForUser(userId)
        );

        String highestCategory = currentMonthCategoryMapTotal.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().name())
                .orElse("N/A");

        double averageDailyExpense = 0.0;
        long totalSpendingDays = expenseRepository.countDistinctSpendingDaysByUserIdAndDateBetween(
                userId,
                monthStart,
                monthEnd
        );

        if (totalSpendingDays > 0) {
            double rawAverage = totalSpent / totalSpendingDays;
            averageDailyExpense = BigDecimal.valueOf(rawAverage)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return new DashboardResponse(
                userId,
                totalSpent,
                highestCategory,
                averageDailyExpense,
                categoryMapTotal,
                currentMonthCategoryMapTotal
        );
    }

        private Map<Category, Double> mapCategoryTotals(List<Object[]> rows) {
                Map<Category, Double> totals = new EnumMap<>(Category.class);

                for (Object[] row : rows) {
                        Category category = (Category) row[0];
                        Number amount = (Number) row[1];
                        totals.put(
                                        category,
                                        BigDecimal.valueOf(amount.doubleValue())
                                                        .setScale(2, RoundingMode.HALF_UP)
                                                        .doubleValue()
                        );
                }

                return totals;
        }
}
