package com.fintrack.fintrack_api.controller;


import com.fintrack.fintrack_api.dto.TransacaoRequestDTO;
import com.fintrack.fintrack_api.dto.TransacaoResponseDTO;
import com.fintrack.fintrack_api.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping("usuario/{usuarioId}")
    public ResponseEntity<TransacaoResponseDTO> criar(
            @PathVariable Long usuarioId,
            @RequestBody @Valid TransacaoRequestDTO dto) {

        TransacaoResponseDTO response = transacaoService.criar(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listar(@PathVariable Long usuarioId) {

        List<TransacaoResponseDTO> response = transacaoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }


}
