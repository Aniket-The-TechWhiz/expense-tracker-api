package com.project.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyCategoryReportResponse {

    private Long userId;
    private String category;
    private int month;
    private int year;
    private double totalAmount;
}