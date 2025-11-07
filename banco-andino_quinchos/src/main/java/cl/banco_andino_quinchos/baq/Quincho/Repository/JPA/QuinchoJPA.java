package cl.banco_andino_quinchos.baq.Quincho.Repository.JPA;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "quinchos")
public class QuinchoJPA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , unique = true)
    private String nombre;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private Boolean disponibilidad;

    // esto no aparecia en el diagrama class
    private String ubacion;
}
