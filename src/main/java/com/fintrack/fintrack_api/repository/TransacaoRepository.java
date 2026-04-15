package com.fintrack.fintrack_api.repository;

import com.fintrack.fintrack_api.model.Transacao;
import com.fintrack.fintrack_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuario(Usuario usuario);

    // Saldo
    @Query("SELECT SUM(CASE WHEN t.tipo = 'RECEITA' THEN t.valor ELSE -t.valor END)" +
            "FROM Transacao t WHERE t.usuario.id = :usuarioId")
    BigDecimal calcularSaldo(@Param("usuarioId") Long usuarioId);

    // Resumo por categoria
    @Query("SELECT t.categoria, SUM(t.valor) FROM Transacao t " +
            "WHERE t.usuario.id = :usuarioId GROUP BY t.categoria")
    List<Object[]> resumoPorCategoria(@Param("usuarioId") Long usuarioId);

    // Extrato por período
    List<Transacao> findByUsuarioAndDataBetween(
            Usuario usuario,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );
}
