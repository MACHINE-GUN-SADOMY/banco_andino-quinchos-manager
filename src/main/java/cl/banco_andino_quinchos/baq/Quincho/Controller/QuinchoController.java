package cl.banco_andino_quinchos.baq.Quincho.Controller;

import cl.banco_andino_quinchos.baq.Quincho.Controller.DTO.Request.ActualizarQuinchoRequest;
import cl.banco_andino_quinchos.baq.Quincho.Controller.DTO.Request.CrearQuinchoRequest;
import cl.banco_andino_quinchos.baq.Quincho.Controller.DTO.Request.EliminarQuinchoRequest;
import cl.banco_andino_quinchos.baq.Quincho.Controller.DTO.Response.QuinchoResponse;
import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Quincho.Service.QuinchoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quinchos")
public class QuinchoController {

    private final QuinchoService quinchoService;

    public QuinchoController(QuinchoService quinchoService) {
        this.quinchoService = quinchoService;
    }

    @PostMapping
    public ResponseEntity<QuinchoResponse> crear(@RequestBody CrearQuinchoRequest request) {
        QuinchoJPA quincho = new QuinchoJPA();
        quincho.setNombre(request.getNombre());
        quincho.setCapacidad(request.getCapacidad());
        quincho.setUbicacion(
                request.getUbicacion() != null ? request.getUbicacion() : "Sin ubicación"
        );
        quincho.setDisponibilidad(
                request.getDisponibilidad() != null ? request.getDisponibilidad() : true
        );

        Integer idAdmin = request.getIdUsuarioAdmin();
        if (idAdmin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        QuinchoJPA creado = quinchoService.crear(quincho, idAdmin);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(creado));
    }

    @GetMapping
    public List<QuinchoResponse> listarTodos(
            @RequestParam(name = "soloActivos", required = false) Boolean soloActivos
    ) {
        List<QuinchoJPA> lista;

        if (Boolean.TRUE.equals(soloActivos)) {
            lista = quinchoService.listarActivos();
        } else {
            lista = quinchoService.listarTodos();
        }

        return lista.stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuinchoResponse> obtenerPorId(@PathVariable Long id) {
        try {
            QuinchoJPA quincho = quinchoService.obtenerPorId(id);
            return ResponseEntity.ok(toResponse(quincho));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuinchoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarQuinchoRequest request
    ) {
        try {
            QuinchoJPA datos = new QuinchoJPA();
            datos.setNombre(request.getNombre());
            datos.setCapacidad(request.getCapacidad());
            datos.setDisponibilidad(request.getDisponibilidad());

            QuinchoJPA actualizado = quinchoService.actualizar(id, datos);
            return ResponseEntity.ok(toResponse(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo
    ) {
        try {
            quinchoService.cambiarEstado(id, activo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id,
            @RequestBody EliminarQuinchoRequest request
    ) {
        if (request.getIdUsuarioAdmin() == null) {
            return ResponseEntity.badRequest().build();
        }

        quinchoService.eliminar(id, request.getIdUsuarioAdmin());
        return ResponseEntity.noContent().build();
    }


    private QuinchoResponse toResponse(QuinchoJPA quincho) {
        return QuinchoResponse.builder()
                .id(quincho.getId())
                .nombre(quincho.getNombre())
                .capacidad(quincho.getCapacidad())
                .disponibilidad(quincho.getDisponibilidad())
                .build();
    }
}