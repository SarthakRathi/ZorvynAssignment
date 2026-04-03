package org.example.zorvyn.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.zorvyn.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private LocalDate date;
    private String description;
}