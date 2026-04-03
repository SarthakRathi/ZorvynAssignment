package org.example.zorvyn.repository;

import org.example.zorvyn.entity.Transaction;
import org.example.zorvyn.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Core filtering requirement from the assignment
    List<Transaction> findByType(TransactionType type);

    // Dashboard API: Get total income or expenses.
    // COALESCE ensures we return 0 instead of null if there are no records yet.
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type")
    BigDecimal getTotalAmountByType(@Param("type") TransactionType type);

    // Dashboard API: Category-wise totals
    @Query("SELECT t.category, COALESCE(SUM(t.amount), 0) FROM Transaction t GROUP BY t.category")
    List<Object[]> getCategoryWiseTotals();
}