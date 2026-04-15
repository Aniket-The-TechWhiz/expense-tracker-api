package com.project.ExpenseTracker.repository;

import com.project.ExpenseTracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByUserId(Long id);
    List<Expense> findByUserIdAndDateBetween(Long id, LocalDateTime startDate, LocalDateTime endDate);
}
