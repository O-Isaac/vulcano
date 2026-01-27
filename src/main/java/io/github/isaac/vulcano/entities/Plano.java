package io.github.isaac.vulcano.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "planos", schema = "fundicion")
public class Plano {
    @Id
    @Column(name = "plano_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // One to one en una sola direccion por que no necesito saber el recurso en que plano pertence, si no
    // tendriamos muchos nulos
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recurso_fabricado_recurso_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Recurso recursoFabricado;

    @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Componente> componentes = new ArrayList<>();

    // Metodos que asegurar la bidireccionalidad
    public void addComponente(Componente componente) {
        componentes.add(componente);
        componente.setPlano(this);
    }

    public void removeComponente(Componente componente) {
        componentes.remove(componente);
        componente.setPlano(null); // Limpia la relación
    }

}