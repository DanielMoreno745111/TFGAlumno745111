package backend.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AbogadoId implements Serializable {
    private static final long serialVersionUID = 488612524010180022L;
    @Column(name = "dni", nullable = false, length = 20)
    private String dni;

    @Column(name = "numero_colegiando", nullable = false, length = 20)
    private String numeroColegiando;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiando() {
        return numeroColegiando;
    }

    public void setNumeroColegiando(String numeroColegiando) {
        this.numeroColegiando = numeroColegiando;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbogadoId entity = (AbogadoId) o;
        return Objects.equals(this.dni, entity.dni) && Objects.equals(this.numeroColegiando, entity.numeroColegiando);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni, numeroColegiando);
    }

}