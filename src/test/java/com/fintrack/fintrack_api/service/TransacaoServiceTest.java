package com.fintrack.fintrack_api.service;

import com.fintrack.fintrack_api.dto.SaldoResponseDTO;
import com.fintrack.fintrack_api.dto.TransacaoRequestDTO;
import com.fintrack.fintrack_api.dto.TransacaoResponseDTO;
import com.fintrack.fintrack_api.model.Categoria;
import com.fintrack.fintrack_api.model.TipoTransacao;
import com.fintrack.fintrack_api.model.Transacao;
import com.fintrack.fintrack_api.model.Usuario;
import com.fintrack.fintrack_api.repository.TransacaoRepository;
import com.fintrack.fintrack_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    private Usuario usuario;
    private Transacao transacaoReceita;
    private Transacao transacaoDespesa;
    private TransacaoRequestDTO dto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Felipe");
        usuario.setEmail("fe@gmail.com");

        dto = new TransacaoRequestDTO(
                new BigDecimal("1500.00"),
                TipoTransacao.RECEITA,
                Categoria.SALARIO,
                "Salário de Abril",
                LocalDateTime.now()
        );

        transacaoReceita = new Transacao();
        transacaoReceita.setId(1L);
        transacaoReceita.setValor(new BigDecimal("3000.00"));
        transacaoReceita.setTipo(TipoTransacao.RECEITA);
        transacaoReceita.setCategoria(Categoria.SALARIO);
        transacaoReceita.setData(LocalDateTime.now());
        transacaoReceita.setUsuario(usuario);

        transacaoDespesa = new Transacao();
        transacaoDespesa.setId(2L);
        transacaoDespesa.setValor(new BigDecimal("1000.00"));
        transacaoDespesa.setTipo(TipoTransacao.DESPESA);
        transacaoDespesa.setCategoria(Categoria.ALIMENTACAO);
        transacaoDespesa.setData(LocalDateTime.now());
        transacaoDespesa.setUsuario(usuario);
    }

    @Test
    void deveCriarTransacaoComSucesso() {
        Transacao transacaoSalva = new Transacao();
        transacaoSalva.setId(1L);
        transacaoSalva.setValor(dto.valor());
        transacaoSalva.setTipo(dto.tipo());
        transacaoSalva.setCategoria(dto.categoria());
        transacaoSalva.setDescricao(dto.descricao());
        transacaoSalva.setData(dto.data());
        transacaoSalva.setUsuario(usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoSalva);

        TransacaoResponseDTO response = transacaoService.criar(1L, dto);

        assertNotNull(response);
        assertEquals(new BigDecimal("1500.00"), response.valor());
        assertEquals(TipoTransacao.RECEITA, response.tipo());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transacaoService.criar(99L, dto));

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(transacaoRepository, never()).save(any());
    }

    @Test
    void deveCalcularSaldoCorretamente() {
        // Seu service busca o saldo pelo repository e também lista as transações
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(transacaoRepository.calcularSaldo(1L)).thenReturn(new BigDecimal("2000.00"));
        when(transacaoRepository.findByUsuario(usuario))
                .thenReturn(List.of(transacaoReceita, transacaoDespesa));

        SaldoResponseDTO saldo = transacaoService.calcularSaldo(1L);

        assertEquals(new BigDecimal("2000.00"), saldo.saldo());
        assertEquals(new BigDecimal("3000.00"), saldo.totalReceitas());
        assertEquals(new BigDecimal("1000.00"), saldo.totalDespesas());
    }

    @Test
    void deveListarTransacoesPorUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(transacaoRepository.findByUsuario(usuario))
                .thenReturn(List.of(transacaoReceita, transacaoDespesa));

        List<TransacaoResponseDTO> resultado = transacaoService.listarPorUsuario(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        assertEquals(new BigDecimal("3000.00"), resultado.get(0).valor());
    }

    @Test
    void deveRetornarSaldoZeroQuandoSemTransacoes() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(transacaoRepository.calcularSaldo(1L)).thenReturn(null);
        when(transacaoRepository.findByUsuario(usuario)).thenReturn(List.of());

        SaldoResponseDTO saldo = transacaoService.calcularSaldo(1L);

        assertEquals(BigDecimal.ZERO, saldo.saldo());
        assertEquals(BigDecimal.ZERO, saldo.totalReceitas());
        assertEquals(BigDecimal.ZERO, saldo.totalDespesas());
    }
}