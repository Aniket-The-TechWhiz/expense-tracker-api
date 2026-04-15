package com.project.ExpenseTracker.service;

import com.project.ExpenseTracker.dto.ExpenseRequest;
import com.project.ExpenseTracker.dto.ExpenseResponse;
import com.project.ExpenseTracker.dto.MonthlyCategoryReportResponse;
import com.project.ExpenseTracker.dto.MonthlyReportResponse;
import com.project.ExpenseTracker.model.Expense;
import com.project.ExpenseTracker.model.User;
import com.project.ExpenseTracker.repository.ExpenseRepository;
import com.project.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseResponse generateExpense(ExpenseRequest request) {
        User user=userRepository.findById(request.getUserId()).orElseThrow(()->new RuntimeException("Invalid user id"+request.getUserId()));
        Expense expense=Expense.builder()
                .user(user)
                .amount(request.getAmount())
                .category(request.getCategory())
                .build();
        Expense savedExpense=expenseRepository.save(expense);
        return mapToResponse(savedExpense);
    }

    private ExpenseResponse mapToResponse(Expense savedExpense) {
        ExpenseResponse expenseResponse=new ExpenseResponse();
        expenseResponse.setId(savedExpense.getId());
        expenseResponse.setUserId(savedExpense.getUser().getId());
        expenseResponse.setAmount(savedExpense.getAmount());
        expenseResponse.setCategory(savedExpense.getCategory());
        expenseResponse.setDate(savedExpense.getDate());
        return expenseResponse;
    }

        public ExpenseResponse updateExpense(Long expenseId, Long userId, ExpenseRequest request) {
                userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Invalid user id " + userId));

                Expense expense = expenseRepository.findById(expenseId)
                                .orElseThrow(() -> new RuntimeException("Expense not found with id " + expenseId));

                if (!expense.getUser().getId().equals(userId)) {
                        throw new RuntimeException("You are not authorized to update this expense");
                }

                expense.setAmount(request.getAmount());
                expense.setCategory(request.getCategory());

                Expense updatedExpense = expenseRepository.save(expense);
                return mapToResponse(updatedExpense);
        }

        public void deleteExpense(Long expenseId, Long userId) {
                userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Invalid user id " + userId));

                Expense expense = expenseRepository.findById(expenseId)
                                .orElseThrow(() -> new RuntimeException("Expense not found with id " + expenseId));

                if (!expense.getUser().getId().equals(userId)) {
                        throw new RuntimeException("You are not authorized to delete this expense");
                }

                expenseRepository.delete(expense);
        }

    public List<ExpenseResponse> getExpensesByUserId(Long id) {
        List<Expense> expenseList = expenseRepository.findByUserId(id);
        return expenseList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

        public List<ExpenseResponse> getExpensesByDateRange(Long userId, String startDate, String endDate) {
                userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                try {
                        LocalDate parsedStartDate = LocalDate.parse(startDate);
                        LocalDate parsedEndDate = LocalDate.parse(endDate);

                        if (parsedStartDate.isAfter(parsedEndDate)) {
                                throw new IllegalArgumentException("startDate cannot be after endDate");
                        }

                        LocalDateTime startDateTime = parsedStartDate.atStartOfDay();
                        LocalDateTime endDateTime = parsedEndDate.atTime(23, 59, 59, 999_999_999);

                        return expenseRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime)
                                        .stream()
                                        .map(this::mapToResponse)
                                        .collect(Collectors.toList());
                } catch (DateTimeParseException ex) {
                        throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd");
                }
        }


    public MonthlyReportResponse getMonthlyExpense(Long userId, int month, int year) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<Expense> expenseList = expenseRepository.findByUserId(userId);


        double total = expenseList.stream()
                .filter(expense ->
                        expense.getDate().getMonthValue() == month &&
                                expense.getDate().getYear() == year
                )
                .mapToDouble(Expense::getAmount)
                .sum();

        return new MonthlyReportResponse(userId, month, year, total);
    }

    public MonthlyCategoryReportResponse getMonthlyExpenseByCategory(Long userId, String category, int month, int year) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }

        List<Expense> expenseList = expenseRepository.findByUserId(userId);


        double total = expenseList.stream()
                .filter(expense ->
                        expense.getDate().getMonthValue() == month &&
                                expense.getDate().getYear() == year &&
                                expense.getCategory().name().equalsIgnoreCase(category)
                )
                .mapToDouble(Expense::getAmount)
                .sum();

        return new MonthlyCategoryReportResponse(userId, category, month, year, total);
    }
}
