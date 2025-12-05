package cl.banco_andino_quinchos.baq.Usuario.Controller.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
