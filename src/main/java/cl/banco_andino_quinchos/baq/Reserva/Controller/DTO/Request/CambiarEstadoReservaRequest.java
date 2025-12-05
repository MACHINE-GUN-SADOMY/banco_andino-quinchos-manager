package cl.banco_andino_quinchos.baq.Reserva.Controller.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarEstadoReservaRequest {
    private String nuevoEstado;  // PENDIENTE, APROBADA, RECHAZADA, CANCELADA
    private Boolean aprobado;    //
    private Integer idAdmin;
}
