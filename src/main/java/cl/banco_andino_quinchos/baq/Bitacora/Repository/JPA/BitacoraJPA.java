package cl.banco_andino_quinchos.baq.Bitacora.Repository.JPA;

import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.expression.spel.ast.NullLiteral;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Table(name = "bitacora")
public class BitacoraJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioJPA usuario;

    @Column(nullable = true)
    private String tipoAccion;

    @Column(nullable = false, length = 200)
    private String accion;

    @Column
    private String detalle;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
