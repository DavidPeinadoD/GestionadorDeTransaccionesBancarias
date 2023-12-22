package io.bootify.gestionador_de_transacciones.repos;

import io.bootify.gestionador_de_transacciones.domain.Cuenta;
import io.bootify.gestionador_de_transacciones.domain.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TarjetaRepository extends JpaRepository<Tarjeta, String> {

    Tarjeta findFirstByTarjetaCuenta(Cuenta cuenta);

    boolean existsByIbanIgnoreCase(String iban);

    boolean existsByNumeroPAN(Long numeroPAN);

}
