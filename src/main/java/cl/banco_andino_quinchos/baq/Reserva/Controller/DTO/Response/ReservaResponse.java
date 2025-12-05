package cl.banco_andino_quinchos.baq.Reserva.Controller.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservaResponse {
    private Integer id;

    private Integer idUsuario;
    private String nombreUsuario;

    private Integer idQuincho;
    private String nombreQuincho;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private String estado;
    private Boolean aprobado;

    private String motivo;
}
