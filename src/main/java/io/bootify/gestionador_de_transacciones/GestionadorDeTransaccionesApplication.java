package io.bootify.gestionador_de_transacciones;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GestionadorDeTransaccionesApplication {

    public static void main(final String[] args) {
        SpringApplication.run(GestionadorDeTransaccionesApplication.class, args);

    }

}
