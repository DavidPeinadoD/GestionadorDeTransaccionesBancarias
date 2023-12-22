package io.bootify.gestionador_de_transacciones.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Set;


@Entity
public class Cuenta {

    @Id
    @Column(nullable = false, updatable = false, length = 24)
    private String iban;

    @Column(nullable = false, unique = true)
    private String titular;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_usuario_id", nullable = false)
    private Usuario cuentaUsuario;

    @OneToMany(mappedBy = "tarjetaCuenta")
    private Set<Tarjeta> cuenta;

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

    public Usuario getCuentaUsuario() {
        return cuentaUsuario;
    }

    public void setCuentaUsuario(final Usuario cuentaUsuario) {
        this.cuentaUsuario = cuentaUsuario;
    }

    public Set<Tarjeta> getCuenta() {
        return cuenta;
    }

    public void setCuenta(final Set<Tarjeta> cuenta) {
        this.cuenta = cuenta;
    }

}
