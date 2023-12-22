package io.bootify.gestionador_de_transacciones.service;

import io.bootify.gestionador_de_transacciones.domain.Cuenta;
import io.bootify.gestionador_de_transacciones.domain.Tarjeta;
import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import io.bootify.gestionador_de_transacciones.domain.Usuario;
import io.bootify.gestionador_de_transacciones.model.CuentaDTO;
import io.bootify.gestionador_de_transacciones.repos.CuentaRepository;
import io.bootify.gestionador_de_transacciones.repos.TarjetaRepository;
import io.bootify.gestionador_de_transacciones.repos.TransaccionRepository;
import io.bootify.gestionador_de_transacciones.repos.UsuarioRepository;
import io.bootify.gestionador_de_transacciones.util.NotFoundException;
import io.bootify.gestionador_de_transacciones.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TarjetaRepository tarjetaRepository;
    private final TransaccionRepository transaccionRepository;

    public CuentaService(final CuentaRepository cuentaRepository,
            final UsuarioRepository usuarioRepository, final TarjetaRepository tarjetaRepository,
            final TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public List<CuentaDTO> findAll() {
        final List<Cuenta> cuentas = cuentaRepository.findAll(Sort.by("iban"));
        return cuentas.stream()
                .map(cuenta -> mapToDTO(cuenta, new CuentaDTO()))
                .toList();
    }

    public CuentaDTO get(final String iban) {
        return cuentaRepository.findById(iban)
                .map(cuenta -> mapToDTO(cuenta, new CuentaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final CuentaDTO cuentaDTO) {
        final Cuenta cuenta = new Cuenta();
        mapToEntity(cuentaDTO, cuenta);
        cuenta.setIban(cuentaDTO.getIban());
        return cuentaRepository.save(cuenta).getIban();
    }

    public void update(final String iban, final CuentaDTO cuentaDTO) {
        final Cuenta cuenta = cuentaRepository.findById(iban)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cuentaDTO, cuenta);
        cuentaRepository.save(cuenta);
    }

    public void delete(final String iban) {
        cuentaRepository.deleteById(iban);
    }

    private CuentaDTO mapToDTO(final Cuenta cuenta, final CuentaDTO cuentaDTO) {
        cuentaDTO.setIban(cuenta.getIban());
        cuentaDTO.setTitular(cuenta.getTitular());
        cuentaDTO.setCuentaUsuario(cuenta.getCuentaUsuario() == null ? null : cuenta.getCuentaUsuario().getId());
        return cuentaDTO;
    }

    private Cuenta mapToEntity(final CuentaDTO cuentaDTO, final Cuenta cuenta) {
        cuenta.setTitular(cuentaDTO.getTitular());
        final Usuario cuentaUsuario = cuentaDTO.getCuentaUsuario() == null ? null : usuarioRepository.findById(cuentaDTO.getCuentaUsuario())
                .orElseThrow(() -> new NotFoundException("cuentaUsuario not found"));
        cuenta.setCuentaUsuario(cuentaUsuario);
        return cuenta;
    }

    public boolean ibanExists(final String iban) {
        return cuentaRepository.existsByIbanIgnoreCase(iban);
    }

    public boolean titularExists(final String titular) {
        return cuentaRepository.existsByTitularIgnoreCase(titular);
    }

    public String getReferencedWarning(final String iban) {
        final Cuenta cuenta = cuentaRepository.findById(iban)
                .orElseThrow(NotFoundException::new);
        final Tarjeta tarjetaCuentaTarjeta = tarjetaRepository.findFirstByTarjetaCuenta(cuenta);
        if (tarjetaCuentaTarjeta != null) {
            return WebUtils.getMessage("cuenta.tarjeta.tarjetaCuenta.referenced", tarjetaCuentaTarjeta.getIban());
        }
        final Transaccion transaccionCuentaTransaccion = transaccionRepository.findFirstByTransaccionCuenta(cuenta);
        if (transaccionCuentaTransaccion != null) {
            return WebUtils.getMessage("cuenta.transaccion.transaccionCuenta.referenced", transaccionCuentaTransaccion.getId());
        }
        return null;
    }

}
