package io.bootify.gestionador_de_transacciones.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


public class TransaccionDTO {

    private Long id;

    @NotNull
    @Size(max = 24)
    @JsonProperty("iBANBeneficiario")
    private String iBANBeneficiario;

    @NotNull
    @Size(max = 24)
    @JsonProperty("iBANTransactor")
    private String iBANTransactor;

    @NotNull
    private Integer cantidad;

    @NotNull
    private LocalDateTime fechaTransaccion;

    @Size(max = 24)
    private String transaccionCuenta;

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

    public String getTransaccionCuenta() {
        return transaccionCuenta;
    }

    public void setTransaccionCuenta(final String transaccionCuenta) {
        this.transaccionCuenta = transaccionCuenta;
    }

}
