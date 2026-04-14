package com.project.ExpenseTracker.dto;

import com.project.ExpenseTracker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    private Long id;
    private double amount;
    private Category category;
    private LocalDateTime date;
    private Long userId;
    private double monthlyReport;
}
