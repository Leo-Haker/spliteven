package se.hem.spliteven.dto;

import java.math.BigDecimal;

public record AccountBalanceDto(Long accountId, String accountName, BigDecimal myBalance) {
}