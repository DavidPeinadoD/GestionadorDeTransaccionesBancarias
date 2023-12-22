package io.bootify.gestionador_de_transacciones.repos;

import io.bootify.gestionador_de_transacciones.domain.Cuenta;
import io.bootify.gestionador_de_transacciones.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CuentaRepository extends JpaRepository<Cuenta, String> {

    Cuenta findFirstByCuentaUsuario(Usuario usuario);

    boolean existsByIbanIgnoreCase(String iban);

    boolean existsByTitularIgnoreCase(String titular);

}
