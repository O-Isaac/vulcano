package io.github.isaac.vulcano.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "componentes", schema = "fundicion")
public class Componente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "componente_id", nullable = false)
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "plano_id")
    private Plano plano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id")
    private Recurso recurso;

    public void setPlano(Plano plano) {
        // Si ya tiene este plano, no hacemos nada para evitar bucles
        if (this.plano == plano) return;

        // Si ya pertenecía a otro plano, nos eliminamos de la lista del anterior
        if (this.plano != null) {
            this.plano.getComponentes().remove(this);
        }

        this.plano = plano;

        // Si el nuevo plano no es nulo, nos añadimos a su lista
        if (plano != null && !plano.getComponentes().contains(this)) {
            plano.getComponentes().add(this);
        }
    }

    public void setRecurso(Recurso recurso) {
        if (this.recurso == recurso) return;

        // Desvincular del recurso anterior si existía
        if (this.recurso != null) {
            this.recurso.getComponentes().remove(this);
        }

        this.recurso = recurso;

        // Vincular al nuevo recurso
        if (recurso != null && !recurso.getComponentes().contains(this)) {
            recurso.getComponentes().add(this);
        }
    }
}