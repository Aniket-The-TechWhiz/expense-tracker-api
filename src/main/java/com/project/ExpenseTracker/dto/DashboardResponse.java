package com.project.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Long userId;
    private double totalSpent;
    private String highestCategory;
    private double averageDailyExpense;
}
