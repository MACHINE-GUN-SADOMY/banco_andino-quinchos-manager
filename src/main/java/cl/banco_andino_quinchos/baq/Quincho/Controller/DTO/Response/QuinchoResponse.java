package cl.banco_andino_quinchos.baq.Quincho.Controller.DTO.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuinchoResponse {
    private Integer id;
    private String nombre;
    private Integer capacidad;
    private Boolean disponibilidad;
}
