package io.bootify.gestionador_de_transacciones.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class CuentaDTO {

    @Size(max = 24)
    private String iban;

    @NotNull
    @Size(max = 255)
    private String titular;

    @NotNull
    private Long cuentaUsuario;

    public String getIban() {
        return iban;
    }

    public void setIban(final String iban) {
        this.iban = iban;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(final String titular) {
        this.titular = titular;
    }

    public Long getCuentaUsuario() {
        return cuentaUsuario;
    }

    public void setCuentaUsuario(final Long cuentaUsuario) {
        this.cuentaUsuario = cuentaUsuario;
    }

}
