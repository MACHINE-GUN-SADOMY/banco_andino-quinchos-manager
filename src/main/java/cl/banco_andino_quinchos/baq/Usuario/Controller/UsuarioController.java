package cl.banco_andino_quinchos.baq.Usuario.Controller;

import cl.banco_andino_quinchos.baq.Usuario.Controller.DTO.Request.ActualizarUsuarioRequest;
import cl.banco_andino_quinchos.baq.Usuario.Controller.DTO.Request.CrearUsuarioRequest;
import cl.banco_andino_quinchos.baq.Usuario.Controller.DTO.Request.LoginRequest;
import cl.banco_andino_quinchos.baq.Usuario.Controller.DTO.Response.UsuarioResponse;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody CrearUsuarioRequest request) {

        UsuarioJPA usuario = new UsuarioJPA();
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(request.getPassword());
        usuario.setRol(request.getRol());
        usuario.setActivo(request.getActivo() != null ? request.getActivo() : true);

        UsuarioJPA creado = usuarioService.crear(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(creado));
    }

    @GetMapping
    public List<UsuarioResponse> listarTodos() {
        return usuarioService.listarTodos()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Integer id) {
        try {
            UsuarioJPA usuario = usuarioService.obtenerPorId(id);
            return ResponseEntity.ok(toResponse(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody ActualizarUsuarioRequest request
    ) {
        try {
            UsuarioJPA datos = new UsuarioJPA();
            datos.setNombre(request.getNombre());
            datos.setCorreo(request.getCorreo());
            datos.setPassword(request.getPassword());
            datos.setRol(request.getRol());
            datos.setActivo(request.getActivo());

            UsuarioJPA actualizado = usuarioService.actualizar(id, datos);
            return ResponseEntity.ok(toResponse(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Integer id) {
        try {
            usuarioService.desactivar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/activar")
    public ResponseEntity<Void> activarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.activar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody LoginRequest request) {
        try {
            UsuarioJPA usuario = usuarioService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(toResponse(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private UsuarioResponse toResponse(UsuarioJPA usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .build();
    }
}