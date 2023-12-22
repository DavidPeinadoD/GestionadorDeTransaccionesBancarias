package io.bootify.gestionador_de_transacciones.service;

import io.bootify.gestionador_de_transacciones.domain.Cuenta;
import io.bootify.gestionador_de_transacciones.domain.Tarjeta;
import io.bootify.gestionador_de_transacciones.model.TarjetaDTO;
import io.bootify.gestionador_de_transacciones.repos.CuentaRepository;
import io.bootify.gestionador_de_transacciones.repos.TarjetaRepository;
import io.bootify.gestionador_de_transacciones.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TarjetaService {

    private final TarjetaRepository tarjetaRepository;
    private final CuentaRepository cuentaRepository;

    public TarjetaService(final TarjetaRepository tarjetaRepository,
            final CuentaRepository cuentaRepository) {
        this.tarjetaRepository = tarjetaRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<TarjetaDTO> findAll() {
        final List<Tarjeta> tarjetas = tarjetaRepository.findAll(Sort.by("iban"));
        return tarjetas.stream()
                .map(tarjeta -> mapToDTO(tarjeta, new TarjetaDTO()))
                .toList();
    }

    public TarjetaDTO get(final String iban) {
        return tarjetaRepository.findById(iban)
                .map(tarjeta -> mapToDTO(tarjeta, new TarjetaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final TarjetaDTO tarjetaDTO) {
        final Tarjeta tarjeta = new Tarjeta();
        mapToEntity(tarjetaDTO, tarjeta);
        tarjeta.setIban(tarjetaDTO.getIban());
        return tarjetaRepository.save(tarjeta).getIban();
    }

    public void update(final String iban, final TarjetaDTO tarjetaDTO) {
        final Tarjeta tarjeta = tarjetaRepository.findById(iban)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tarjetaDTO, tarjeta);
        tarjetaRepository.save(tarjeta);
    }

    public void delete(final String iban) {
        tarjetaRepository.deleteById(iban);
    }

    private TarjetaDTO mapToDTO(final Tarjeta tarjeta, final TarjetaDTO tarjetaDTO) {
        tarjetaDTO.setIban(tarjeta.getIban());
        tarjetaDTO.setNumeroPAN(tarjeta.getNumeroPAN());
        tarjetaDTO.setCcv(tarjeta.getCcv());
        tarjetaDTO.setTarjetaCuenta(tarjeta.getTarjetaCuenta() == null ? null : tarjeta.getTarjetaCuenta().getIban());
        return tarjetaDTO;
    }

    private Tarjeta mapToEntity(final TarjetaDTO tarjetaDTO, final Tarjeta tarjeta) {
        tarjeta.setNumeroPAN(tarjetaDTO.getNumeroPAN());
        tarjeta.setCcv(tarjetaDTO.getCcv());
        final Cuenta tarjetaCuenta = tarjetaDTO.getTarjetaCuenta() == null ? null : cuentaRepository.findById(tarjetaDTO.getTarjetaCuenta())
                .orElseThrow(() -> new NotFoundException("tarjetaCuenta not found"));
        tarjeta.setTarjetaCuenta(tarjetaCuenta);
        return tarjeta;
    }

    public boolean ibanExists(final String iban) {
        return tarjetaRepository.existsByIbanIgnoreCase(iban);
    }

    public boolean numeroPANExists(final Long numeroPAN) {
        return tarjetaRepository.existsByNumeroPAN(numeroPAN);
    }

}
