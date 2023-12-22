package io.bootify.gestionador_de_transacciones.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Tarjeta {

    @Id
    @Column(nullable = false, updatable = false, length = 24)
    private String iban;

    @Column(nullable = false, unique = true)
    private Long numeroPAN;

    @Column(nullable = false)
    private Integer ccv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarjeta_cuenta_id", nullable = false)
    private Cuenta tarjetaCuenta;

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

    public Cuenta getTarjetaCuenta() {
        return tarjetaCuenta;
    }

    public void setTarjetaCuenta(final Cuenta tarjetaCuenta) {
        this.tarjetaCuenta = tarjetaCuenta;
    }

}
