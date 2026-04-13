package com.fintrack.fintrack_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    private String descricao;

    @Column(nullable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


}
