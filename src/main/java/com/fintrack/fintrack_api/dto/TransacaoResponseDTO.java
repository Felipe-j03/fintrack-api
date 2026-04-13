package com.fintrack.fintrack_api.dto;

import com.fintrack.fintrack_api.model.Categoria;
import com.fintrack.fintrack_api.model.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(
        Long id,
        BigDecimal valor,
        TipoTransacao tipo,
        Categoria categoria,
        String descricao,
        LocalDateTime data
) {}
