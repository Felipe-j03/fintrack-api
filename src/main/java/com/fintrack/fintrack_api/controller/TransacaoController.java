package com.fintrack.fintrack_api.controller;


import com.fintrack.fintrack_api.dto.CategoriaResumoDTO;
import com.fintrack.fintrack_api.dto.SaldoResponseDTO;
import com.fintrack.fintrack_api.dto.TransacaoRequestDTO;
import com.fintrack.fintrack_api.dto.TransacaoResponseDTO;
import com.fintrack.fintrack_api.model.Usuario;
import com.fintrack.fintrack_api.repository.UsuarioRepository;
import com.fintrack.fintrack_api.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuario(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<TransacaoResponseDTO> criar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid TransacaoRequestDTO dto) {

        Usuario usuario = getUsuario(userDetails);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transacaoService.criar(usuario.getId(), dto));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listar(@AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = getUsuario(userDetails);
        return ResponseEntity.ok(transacaoService.listarPorUsuario(usuario.getId()));
    }

    @GetMapping("/usuario/{usuarioId}/saldo")
    public ResponseEntity<SaldoResponseDTO> saldo(@AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = getUsuario(userDetails);
        return ResponseEntity.ok(transacaoService.calcularSaldo(usuario.getId()));
    }

    @GetMapping("/usuario/{usuarioId}/categorias")
    public ResponseEntity<List<CategoriaResumoDTO>> categorias(@AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = getUsuario(userDetails);
        return ResponseEntity.ok(transacaoService.resumoPorCategoria(usuario.getId()));
    }

    @GetMapping("/usuario/{usuarioId}/extrato")
    public ResponseEntity<List<TransacaoResponseDTO>> extrato(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {

        Usuario usuario = getUsuario(userDetails);
        return ResponseEntity.ok(transacaoService.extratoPorPeriodo(usuario.getId(), inicio, fim));
    }


}
