package com.fintrack.fintrack_api.dto;

import com.fintrack.fintrack_api.model.Categoria;

import java.math.BigDecimal;

public record CategoriaResumoDTO(
        Categoria categoria,
        BigDecimal total
) {
}
