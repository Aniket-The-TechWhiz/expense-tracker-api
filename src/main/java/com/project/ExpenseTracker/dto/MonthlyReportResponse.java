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
}
