package com.project.ExpenseTracker.repository;

import com.project.ExpenseTracker.model.Category;
import com.project.ExpenseTracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByUserId(Long id);
    List<Expense> findByUserIdAndDateBetween(Long id, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.date BETWEEN :startDate AND :endDate
        """)
    double sumAmountByUserIdAndDateBetween(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.category = :category
          AND e.date BETWEEN :startDate AND :endDate
        """)
    double sumAmountByUserIdAndCategoryAndDateBetween(
        @Param("userId") Long userId,
        @Param("category") Category category,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT e.category, COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.date BETWEEN :startDate AND :endDate
        GROUP BY e.category
        """)
    List<Object[]> sumAmountByCategoryForDateRange(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT e.category, COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
        GROUP BY e.category
        """)
    List<Object[]> sumAmountByCategoryForUser(@Param("userId") Long userId);

    @Query("""
        SELECT COUNT(DISTINCT FUNCTION('DATE', e.date))
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.date BETWEEN :startDate AND :endDate
        """)
    long countDistinctSpendingDaysByUserIdAndDateBetween(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
