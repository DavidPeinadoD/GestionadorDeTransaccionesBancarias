package io.bootify.gestionador_de_transacciones.repos;

import io.bootify.gestionador_de_transacciones.domain.Cuenta;
import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    Transaccion findFirstByTransaccionCuenta(Cuenta cuenta);

}
