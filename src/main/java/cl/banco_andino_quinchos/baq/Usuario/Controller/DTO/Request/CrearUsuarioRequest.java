package cl.banco_andino_quinchos.baq.Usuario.Controller.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearUsuarioRequest {
    private String nombre;
    private String correo;
    private String password;
    private String rol;      // "ADMIN" / "EMPLEADO"
    private Boolean activo;
}
