package io.bootify.gestionador_de_transacciones.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class TarjetaDTO {

    @Size(max = 24)
    private String iban;

    @NotNull
    private Long numeroPAN;

    @NotNull
    private Integer ccv;

    @NotNull
    @Size(max = 24)
    private String tarjetaCuenta;

    public String getIban() {
        return iban;
    }

    public void setIban(final String iban) {
        this.iban = iban;
    }

    public Long getNumeroPAN() {
        return numeroPAN;
    }

    public void setNumeroPAN(final Long numeroPAN) {
        this.numeroPAN = numeroPAN;
    }

    public Integer getCcv() {
        return ccv;
    }

    public void setCcv(final Integer ccv) {
        this.ccv = ccv;
    }

    public String getTarjetaCuenta() {
        return tarjetaCuenta;
    }

    public void setTarjetaCuenta(final String tarjetaCuenta) {
        this.tarjetaCuenta = tarjetaCuenta;
    }

}
