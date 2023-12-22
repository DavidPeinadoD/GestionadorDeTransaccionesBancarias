package io.bootify.gestionador_de_transacciones.config;

import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import io.bootify.gestionador_de_transacciones.repos.TransaccionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbInit {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @PostConstruct
    private void postConstruct() {
        final Transaccion transaccion = new Transaccion();
        transaccion.setId(1001L);
        transaccion.setIBANBeneficiario("ES6621000418401234567891");
        transaccion.setIBANTransactor("ES6621000418401234567891");
        transaccion.setCantidad(100);
        transaccion.setFechaTransaccion(java.time.LocalDateTime.now());
        transaccionRepository.save(transaccion);
        System.out.println("DB INIT");
    }
}