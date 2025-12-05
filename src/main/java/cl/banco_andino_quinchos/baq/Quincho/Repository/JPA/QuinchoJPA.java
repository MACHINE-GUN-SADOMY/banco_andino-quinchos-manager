package cl.banco_andino_quinchos.baq.Quincho.Repository.JPA;

import jakarta.persistence.*;
import lombok.*;

@Entity
<<<<<<< HEAD
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
=======
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
>>>>>>> 0bcdbe79853ecefe228c945a5352bf4ecaff9988
@Table(name = "quinchos")
public class QuinchoJPA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , unique = true)
    private String nombre;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
<<<<<<< HEAD
    private Boolean disponibilidad = true;

    private String ubicacion;
=======
    private Boolean disponibilidad;

    // esto no aparecia en el diagrama class
    private String ubacion;
>>>>>>> 0bcdbe79853ecefe228c945a5352bf4ecaff9988
}
