package cl.banco_andino_quinchos.baq.Usuario.Service;

import cl.banco_andino_quinchos.baq.Bitacora.Service.BitacoraService;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.UsuarioJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioJpaRepository usuarioRepository;
    private final BitacoraService bitacoraService;

    public UsuarioService(UsuarioJpaRepository usuarioRepository, BitacoraService bitacoraService) {
        this.usuarioRepository = usuarioRepository;
        this.bitacoraService = bitacoraService;
    }

    public UsuarioJPA crear(UsuarioJPA usuario) {
        UsuarioJPA creado = usuarioRepository.save(usuario);

        bitacoraService.registrarAccion(
                "CREAR_USUARIO",
                "Usuario " + creado.getCorreo() + " creado",
                "Se a creado un usuario ",
                creado
        );

        return creado;
    }

    public List<UsuarioJPA> listarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioJPA obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public UsuarioJPA actualizar(Integer id, UsuarioJPA datosActualizados) {
        UsuarioJPA existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setNombre(datosActualizados.getNombre());
        existente.setCorreo(datosActualizados.getCorreo());
        existente.setRol(datosActualizados.getRol());
        existente.setActivo(datosActualizados.getActivo());

        UsuarioJPA actualizado = usuarioRepository.save(existente);

        bitacoraService.registrarAccion(
                "MODIFICAR_USUARIO",
                "Usuario " + actualizado.getCorreo() + " modificado",
                "Se a modifico usuario ",
                actualizado
        );

        return actualizado;
    }

    public void desactivar(Integer id) {
        UsuarioJPA usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);

        bitacoraService.registrarAccion(
                "DESACTIVAR_USUARIO",
                "Usuario " + usuario.getCorreo() + " desactivado",
                "Se a desactivado un usuario ",
                usuario
        );
    }

    public void activar(Integer id) {
        UsuarioJPA usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        bitacoraService.registrarAccion(
                "ACTIVAR_USUARIO",
                "Usuario " + usuario.getCorreo() + " desactivado",
                "Se a activado un usuario ",
                usuario
        );
    }

    public UsuarioJPA login(String email, String password) {
        UsuarioJPA usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

                bitacoraService.registrarAccion(
                        "LOGIN",
                        "Inicio de sesión invalido de " + email,
                        "Creedenciales incorrectas",
                        usuario
                );

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Credenciales inválidas");
        }

        bitacoraService.registrarAccion(
                "LOGIN",
                "Inicio de sesión exitoso de " + email,
                "Se a iniciado Sesion ",
                usuario
        );
        return usuario;
    }


}
