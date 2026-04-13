package com.fintrack.fintrack_api.repository;

import com.fintrack.fintrack_api.model.Transacao;
import com.fintrack.fintrack_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuario(Usuario usuario);
}
