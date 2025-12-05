package cl.banco_andino_quinchos.baq.Reserva.Repository.JPA;

import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "reserva")
public class ReservaJPA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private java.time.LocalDateTime fechaInicio;

    @Column(nullable = false)
    private java.time.LocalDateTime fechaFin;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Boolean aprobado;

    @ManyToOne @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioJPA usuario;

    @ManyToOne @JoinColumn(name = "id_quincho", nullable = false)
    private QuinchoJPA quincho;

    @Column(nullable = true)
    private String motivo;
}
