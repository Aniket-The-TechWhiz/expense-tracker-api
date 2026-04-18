package com.project.ExpenseTracker.dto;

import com.project.ExpenseTracker.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {

    private double amount;

    private Category category;
}
