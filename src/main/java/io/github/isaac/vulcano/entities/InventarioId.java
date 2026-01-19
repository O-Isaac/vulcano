package io.github.isaac.vulcano.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class InventarioId implements Serializable {
    private static final long serialVersionUID = -4837178172115641723L;
    @Column(name = "recurso_id", nullable = false)
    private Integer recursoId;

    @Column(name = "jugador_id", nullable = false)
    private Integer jugadorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InventarioId entity = (InventarioId) o;
        return Objects.equals(this.jugadorId, entity.jugadorId) &&
                Objects.equals(this.recursoId, entity.recursoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jugadorId, recursoId);
    }

}