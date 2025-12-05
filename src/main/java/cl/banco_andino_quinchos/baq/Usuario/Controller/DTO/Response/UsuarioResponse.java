package cl.banco_andino_quinchos.baq.Usuario.Controller.DTO.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsuarioResponse {
    private Integer id;
    private String nombre;
    private String correo;
    private String rol;
    private Boolean activo;
}
