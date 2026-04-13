package com.fintrack.fintrack_api.service;

import com.fintrack.fintrack_api.dto.TransacaoRequestDTO;
import com.fintrack.fintrack_api.dto.TransacaoResponseDTO;
import com.fintrack.fintrack_api.model.Transacao;
import com.fintrack.fintrack_api.model.Usuario;
import com.fintrack.fintrack_api.repository.TransacaoRepository;
import com.fintrack.fintrack_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
