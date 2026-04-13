package com.fintrack.fintrack_api.dto;

import com.fintrack.fintrack_api.model.Categoria;
import com.fintrack.fintrack_api.model.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoRequestDTO(

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "Tipo é obrigatório")
        TipoTransacao tipo,

        @NotNull(message = "Categoria é obrigatória")
        Categoria categoria,

        String descricao,

        @NotNull(message = "Data é obrigatória")
        LocalDateTime data
) {}
