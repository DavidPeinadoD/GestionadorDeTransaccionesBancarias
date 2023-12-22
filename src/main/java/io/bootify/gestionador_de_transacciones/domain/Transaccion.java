package io.bootify.gestionador_de_transacciones.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDateTime;


@Entity
public class Transaccion {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false, length = 24)
    private String iBANBeneficiario;

    @Column(nullable = false, length = 24)
    private String iBANTransactor;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private LocalDateTime fechaTransaccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaccion_cuenta_id")
    private Cuenta transaccionCuenta;

    public Transaccion(long id, String ibanbeneficiario, String ibantransactor, int cantidad, LocalDateTime fechaTransaccion) {
    }

    public Transaccion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getIBANBeneficiario() {
        return iBANBeneficiario;
    }

    public void setIBANBeneficiario(final String iBANBeneficiario) {
        this.iBANBeneficiario = iBANBeneficiario;
    }

    public String getIBANTransactor() {
        return iBANTransactor;
    }

    public void setIBANTransactor(final String iBANTransactor) {
        this.iBANTransactor = iBANTransactor;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(final Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(final LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Cuenta getTransaccionCuenta() {
        return transaccionCuenta;
    }

    public void setTransaccionCuenta(final Cuenta transaccionCuenta) {
        this.transaccionCuenta = transaccionCuenta;
    }

}
