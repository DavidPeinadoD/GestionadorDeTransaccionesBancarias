package io.bootify.gestionador_de_transacciones.Batch;

import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class TransaccionItemProcessor implements ItemProcessor<Transaccion, Transaccion> {

    private static final Logger log =
            LoggerFactory.getLogger(TransaccionItemProcessor.class);

    @Override
    public Transaccion process(final Transaccion transaccion)
            throws Exception {
        final long id= transaccion.getId();
        final String ibanbeneficiario = transaccion.getIBANBeneficiario().toUpperCase();
        final String ibantransactor = transaccion.getIBANTransactor().toUpperCase();
        final int cantidad = transaccion.getCantidad();
        final java.time.LocalDateTime fechaTransaccion = transaccion.getFechaTransaccion();
        final Transaccion transformedTransaccion = new Transaccion(id, ibanbeneficiario, ibantransactor, cantidad, fechaTransaccion);
        log.info("Converting (" + transaccion + ") into (" + transformedTransaccion + ")");

        return transformedTransaccion;
    }
}