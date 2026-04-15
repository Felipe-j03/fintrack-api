package com.fintrack.fintrack_api.service;

import com.fintrack.fintrack_api.dto.CategoriaResumoDTO;
import com.fintrack.fintrack_api.dto.SaldoResponseDTO;
import com.fintrack.fintrack_api.dto.TransacaoRequestDTO;
import com.fintrack.fintrack_api.dto.TransacaoResponseDTO;
import com.fintrack.fintrack_api.model.Transacao;
import com.fintrack.fintrack_api.model.Usuario;
import com.fintrack.fintrack_api.repository.TransacaoRepository;
import com.fintrack.fintrack_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public TransacaoResponseDTO criar(Long usuarioId, TransacaoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Transacao transacao = new Transacao();
        transacao.setValor(dto.valor());
        transacao.setTipo(dto.tipo());
        transacao.setCategoria(dto.categoria());
        transacao.setDescricao(dto.descricao());
        transacao.setData(dto.data());
        transacao.setUsuario(usuario);

        Transacao salva = transacaoRepository.save(transacao);
        return toDTO(salva);
    }

    public List<TransacaoResponseDTO> listarPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return transacaoRepository.findByUsuario(usuario)
                .stream()
                .map(this::toDTO)
                .toList();

    }

    public SaldoResponseDTO calcularSaldo(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        BigDecimal saldo = transacaoRepository.calcularSaldo(usuarioId);
        if (saldo == null) saldo = BigDecimal.ZERO;

        List<Transacao> transacoes = transacaoRepository
                .findByUsuario(usuarioRepository.findById(usuarioId).get());

        BigDecimal totalReceitas = transacoes.stream()
                .filter(t -> t.getTipo().name().equals("RECEITA"))
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = transacoes.stream()
                .filter(t -> t.getTipo().name().equals("DESPESA"))
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SaldoResponseDTO(saldo, totalReceitas, totalDespesas);
    }

    public List<CategoriaResumoDTO> resumoPorCategoria(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return transacaoRepository.resumoPorCategoria(usuarioId)
                .stream()
                .map(row -> new CategoriaResumoDTO(
                        (com.fintrack.fintrack_api.model.Categoria) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public List<TransacaoResponseDTO> extratoPorPeriodo(
            Long usuarioId, LocalDateTime inicio, LocalDateTime fim) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return transacaoRepository.findByUsuarioAndDataBetween(usuario, inicio, fim)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private TransacaoResponseDTO toDTO(Transacao t) {
        return new TransacaoResponseDTO(
                t.getId(),
                t.getValor(),
                t.getTipo(),
                t.getCategoria(),
                t.getDescricao(),
                t.getData()
        );
    }
}
