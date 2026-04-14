package com.project.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportResponse {

    private Long userId;
    private int month;
    private int year;
    private double totalAmount;
    private String category;

    public MonthlyReportResponse(Long userId, int month, int year, double totalAmount) {
        this.userId = userId;
        this.month = month;
        this.year = year;
        this.totalAmount = totalAmount;
        this.category = null;
    }
}
