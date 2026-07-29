package se.hem.spliteven.dto;

public record CreateExpenseRequest(Long accountId, Long paidById, boolean income,
        String description, java.math.BigDecimal amount,
        java.time.LocalDate date) {
}