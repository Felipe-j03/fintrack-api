package com.fintrack.fintrack_api.dto;

import java.math.BigDecimal;

public record SaldoResponseDTO(
        BigDecimal saldo,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas
) {
}
