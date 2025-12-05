package cl.banco_andino_quinchos.baq.Usuario.Repository.JPA;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "usuario")
public class UsuarioJPA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
<<<<<<< HEAD
    private String nombre;
=======
    private String email;
>>>>>>> 0bcdbe79853ecefe228c945a5352bf4ecaff9988

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private String password;
}
