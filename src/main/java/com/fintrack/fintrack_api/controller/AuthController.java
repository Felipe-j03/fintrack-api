package com.fintrack.fintrack_api.controller;

import com.fintrack.fintrack_api.dto.CadastroRequestDTO;
import com.fintrack.fintrack_api.dto.LoginRequestDTO;
import com.fintrack.fintrack_api.dto.LoginResponseDTO;
import com.fintrack.fintrack_api.service.JwtService;
import com.fintrack.fintrack_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody CadastroRequestDTO dto) {
        usuarioService.cadastrar(dto.nome(), dto.email(), dto.senha());
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        String token = jwtService.gerarToken(dto.email());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
