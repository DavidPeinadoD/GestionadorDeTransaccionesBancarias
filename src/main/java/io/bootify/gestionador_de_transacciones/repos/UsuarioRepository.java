package io.bootify.gestionador_de_transacciones.repos;

import io.bootify.gestionador_de_transacciones.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
