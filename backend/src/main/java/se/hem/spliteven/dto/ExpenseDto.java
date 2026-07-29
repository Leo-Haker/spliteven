package se.hem.spliteven.dto;

import java.math.BigDecimal;

public record ExpenseDto(Long id, String description, BigDecimal amount,
        boolean income, String paidByName, java.time.LocalDate date) {
}