package com.fintrack.fintrack_api.repository;

import com.fintrack.fintrack_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
}
