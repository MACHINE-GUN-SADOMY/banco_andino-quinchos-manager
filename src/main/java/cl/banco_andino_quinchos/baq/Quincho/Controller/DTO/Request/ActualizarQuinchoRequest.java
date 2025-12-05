package cl.banco_andino_quinchos.baq.Quincho.Controller.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarQuinchoRequest {

    private String nombre;
    private Integer capacidad;
    private Boolean disponibilidad;
}
