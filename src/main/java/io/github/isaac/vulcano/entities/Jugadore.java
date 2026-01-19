package io.github.isaac.vulcano.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "jugadores", schema = "fundicion")
public class Jugadore {
    @Id
    @Column(name = "jugador_id", nullable = false)
    private Integer id;

    @ColumnDefault("0")
    @Column(name = "nivel")
    private Integer nivel;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "correo", length = 100)
    private String correo;

}