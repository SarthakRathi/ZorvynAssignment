package org.example.zorvyn.service;

import lombok.RequiredArgsConstructor;
import org.example.zorvyn.dto.request.TransactionRequestDTO;
import org.example.zorvyn.dto.response.TransactionResponseDTO;
import org.example.zorvyn.entity.Transaction;
import org.example.zorvyn.exception.ResourceNotFoundException;
import org.example.zorvyn.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO) {
        Transaction transaction = Transaction.builder()
                .amount(requestDTO.getAmount())
                .type(requestDTO.getType())
                .category(requestDTO.getCategory())
                .date(requestDTO.getDate())
                .description(requestDTO.getDescription())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponseDTO(savedTransaction);
    }

    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO requestDTO) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        existingTransaction.setAmount(requestDTO.getAmount());
        existingTransaction.setType(requestDTO.getType());
        existingTransaction.setCategory(requestDTO.getCategory());
        existingTransaction.setDate(requestDTO.getDate());
        existingTransaction.setDescription(requestDTO.getDescription());

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return mapToResponseDTO(updatedTransaction);
    }

    public List<TransactionResponseDTO> getAllTransactions(String type, String category, String date) {
        return transactionRepository.findAll().stream()
                // Filter by type if provided
                .filter(t -> type == null || t.getType().name().equalsIgnoreCase(type))
                // Filter by category if provided
                .filter(t -> category == null || t.getCategory().equalsIgnoreCase(category))
                // Filter by date if provided
                .filter(t -> date == null || t.getDate().toString().equals(date))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
        return mapToResponseDTO(transaction);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + id);
        }
        transactionRepository.deleteById(id);
    }

    // Helper method to convert Entity to DTO
    private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .build();
    }
}