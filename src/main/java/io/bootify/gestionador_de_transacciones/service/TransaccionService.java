package io.bootify.gestionador_de_transacciones.service;

import io.bootify.gestionador_de_transacciones.domain.Cuenta;
import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import io.bootify.gestionador_de_transacciones.model.TransaccionDTO;
import io.bootify.gestionador_de_transacciones.repos.CuentaRepository;
import io.bootify.gestionador_de_transacciones.repos.TransaccionRepository;
import io.bootify.gestionador_de_transacciones.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public TransaccionService(final TransaccionRepository transaccionRepository,
            final CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<TransaccionDTO> findAll() {
        final List<Transaccion> transaccions = transaccionRepository.findAll(Sort.by("id"));
        return transaccions.stream()
                .map(transaccion -> mapToDTO(transaccion, new TransaccionDTO()))
                .toList();
    }

    public TransaccionDTO get(final Long id) {
        return transaccionRepository.findById(id)
                .map(transaccion -> mapToDTO(transaccion, new TransaccionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TransaccionDTO transaccionDTO) {
        final Transaccion transaccion = new Transaccion();
        mapToEntity(transaccionDTO, transaccion);
        return transaccionRepository.save(transaccion).getId();
    }

    public void update(final Long id, final TransaccionDTO transaccionDTO) {
        final Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transaccionDTO, transaccion);
        transaccionRepository.save(transaccion);
    }

    public void delete(final Long id) {
        transaccionRepository.deleteById(id);
    }

    private TransaccionDTO mapToDTO(final Transaccion transaccion,
            final TransaccionDTO transaccionDTO) {
        transaccionDTO.setId(transaccion.getId());
        transaccionDTO.setIBANBeneficiario(transaccion.getIBANBeneficiario());
        transaccionDTO.setIBANTransactor(transaccion.getIBANTransactor());
        transaccionDTO.setCantidad(transaccion.getCantidad());
        transaccionDTO.setFechaTransaccion(transaccion.getFechaTransaccion());
        transaccionDTO.setTransaccionCuenta(transaccion.getTransaccionCuenta() == null ? null : transaccion.getTransaccionCuenta().getIban());
        return transaccionDTO;
    }

    private Transaccion mapToEntity(final TransaccionDTO transaccionDTO,
            final Transaccion transaccion) {
        transaccion.setIBANBeneficiario(transaccionDTO.getIBANBeneficiario());
        transaccion.setIBANTransactor(transaccionDTO.getIBANTransactor());
        transaccion.setCantidad(transaccionDTO.getCantidad());
        transaccion.setFechaTransaccion(transaccionDTO.getFechaTransaccion());
        final Cuenta transaccionCuenta = transaccionDTO.getTransaccionCuenta() == null ? null : cuentaRepository.findById(transaccionDTO.getTransaccionCuenta())
                .orElseThrow(() -> new NotFoundException("transaccionCuenta not found"));
        transaccion.setTransaccionCuenta(transaccionCuenta);
        return transaccion;
    }

}
