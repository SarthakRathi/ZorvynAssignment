package org.example.zorvyn.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zorvyn.dto.request.TransactionRequestDTO;
import org.example.zorvyn.dto.response.TransactionResponseDTO;
import org.example.zorvyn.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // Only Admins can create records
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO requestDTO) {
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(requestDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    // Viewers, Analysts, and Admins can view records
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // Viewers, Analysts, and Admins can view specific records
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    // Only Admins can delete records
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}