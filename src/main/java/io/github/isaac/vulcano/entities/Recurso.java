package io.github.isaac.vulcano.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "recursos", schema = "fundicion")
public class Recurso {
    @Id
    @Column(name = "recurso_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "`desc`", length = 100)
    private String desc;

    @Column(name = "rareza", length = 100)
    private String rareza;

    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL)
    private List<Componente> componentes = new ArrayList<>();

    public void addComponente(Componente componente) {
        this.componentes.add(componente);

        if (componente.getRecurso() != this) {
            componente.setRecurso(this);
        }
    }

    public void removeComponente(Componente componente) {
        this.componentes.remove(componente);

        if (componente.getRecurso() == this) {
            componente.setRecurso(null);
        }
    }
}