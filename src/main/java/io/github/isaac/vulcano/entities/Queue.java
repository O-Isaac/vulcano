package io.github.isaac.vulcano.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "queues", schema = "fundicion")
public class Queue {
    @Id
    @Column(name = "queue_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ColumnDefault("current_timestamp()")
    @Column(name = "inicio_time")
    private Instant inicioTime;

    @Column(name = "final_time")
    private Instant finalTime;

    @Column(name = "estado", length = 100)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "plano_id")
    private Plano plano;

}