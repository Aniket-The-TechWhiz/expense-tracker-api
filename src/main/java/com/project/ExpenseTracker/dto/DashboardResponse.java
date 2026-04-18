package com.project.ExpenseTracker.dto;

import com.project.ExpenseTracker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Long userId;
    private double totalSpent;
    private String highestCategory;
    private double averageDailyExpense;
    private Map<Category,Double> categoryMapTotal;
    private Map<Category,Double> currentMonthCategoryMapTotal;
}
