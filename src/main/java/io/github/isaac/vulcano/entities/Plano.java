package io.github.isaac.vulcano.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "planos", schema = "fundicion")
public class Plano {
    @Id
    @Column(name = "plano_id", nullable = false)
    private Integer id;

    @ColumnDefault("0")
    @Column(name = "coste")
    private Long coste;

    @Column(name = "`desc`", length = 100)
    private String desc;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "tiempo_construcion")
    private Long tiempoConstrucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "objeto_id")
    private Objeto objeto;

}