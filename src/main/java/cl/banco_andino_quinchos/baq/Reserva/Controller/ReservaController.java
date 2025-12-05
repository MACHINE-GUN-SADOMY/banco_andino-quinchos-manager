package cl.banco_andino_quinchos.baq.Reserva.Controller;

import ch.qos.logback.core.model.Model;
import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Reserva.Controller.DTO.Request.CambiarEstadoReservaRequest;
import cl.banco_andino_quinchos.baq.Reserva.Controller.DTO.Request.CrearReservaRequest;
import cl.banco_andino_quinchos.baq.Reserva.Controller.DTO.Response.ReservaResponse;
import cl.banco_andino_quinchos.baq.Reserva.Repository.JPA.ReservaJPA;
import cl.banco_andino_quinchos.baq.Reserva.Service.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@RequestBody CrearReservaRequest request) {

        ReservaJPA reserva = reservaService.crearReserva(
                request.getIdUsuario(),
                request.getIdQuincho(),
                request.getFechaInicio(),
                request.getFechaFin(),
                request.getMotivo()
        );

        ReservaResponse response = toResponse(reserva);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ReservaResponse> listarTodas() {
        return reservaService.listarTodas()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Integer id) {
        try {
            ReservaJPA reserva = reservaService.obtenerPorId(id);
            return ResponseEntity.ok(toResponse(reserva));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ReservaResponse> listarPorUsuario(@PathVariable Integer idUsuario) {
        return reservaService.listarPorUsuario(idUsuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/quincho/{idQuincho}")
    public List<ReservaResponse> listarPorQuincho(@PathVariable Integer idQuincho) {
        return reservaService.listarPorQuincho(idQuincho)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/{idReserva}/estado")
    public ResponseEntity<ReservaResponse> cambiarEstado(
            @PathVariable Integer idReserva,
            @RequestBody CambiarEstadoReservaRequest request
    ) {
        try {
            ReservaJPA actualizada = reservaService.cambiarEstado(
                    idReserva,
                    request.getNuevoEstado(),
                    request.getAprobado(),
                    request.getIdAdmin()
            );
            return ResponseEntity.ok(toResponse(actualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{idReserva}/cancelar")
    public ResponseEntity<ReservaResponse> cancelarPorEmpleado(
            @PathVariable Integer idReserva,
            @RequestParam Integer idUsuario
    ) {
        try {
            ReservaJPA cancelada = reservaService.cancelarPorUsuario(idReserva, idUsuario);
            return ResponseEntity.ok(toResponse(cancelada));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private ReservaResponse toResponse(ReservaJPA reserva) {
        return ReservaResponse.builder()
                .id(reserva.getId())
                .idUsuario(reserva.getUsuario().getId())
                .nombreUsuario(reserva.getUsuario().getNombre())
                .idQuincho(reserva.getQuincho().getId())
                .nombreQuincho(reserva.getQuincho().getNombre())
                .fechaInicio(reserva.getFechaInicio())
                .fechaFin(reserva.getFechaFin())
                .estado(reserva.getEstado())
                .aprobado(reserva.getAprobado())
                .motivo(reserva.getMotivo())
                .build();
    }
}