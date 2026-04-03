package org.example.zorvyn.service;

import lombok.RequiredArgsConstructor;
import org.example.zorvyn.entity.TransactionType;
import org.example.zorvyn.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public Map<String, Object> getDashboardSummary() {
        BigDecimal totalIncome = transactionRepository.getTotalAmountByType(TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.getTotalAmountByType(TransactionType.EXPENSE);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        // Fetch category totals and format them nicely
        List<Object[]> categoryData = transactionRepository.getCategoryWiseTotals();
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        for (Object[] row : categoryData) {
            categoryTotals.put((String) row[0], (BigDecimal) row[1]);
        }

        // Package everything into a map to return as JSON
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("netBalance", netBalance);
        summary.put("categoryWiseTotals", categoryTotals);

        return summary;
    }
}