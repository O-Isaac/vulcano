package io.github.isaac.vulcano.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

}