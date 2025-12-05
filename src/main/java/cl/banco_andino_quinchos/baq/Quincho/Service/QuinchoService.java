package cl.banco_andino_quinchos.baq.Quincho.Service;

import cl.banco_andino_quinchos.baq.Bitacora.Repository.BitacoraJpaRepository;
import cl.banco_andino_quinchos.baq.Bitacora.Repository.JPA.BitacoraJPA;
import cl.banco_andino_quinchos.baq.Bitacora.Service.BitacoraService;
import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Quincho.Repository.QuinchoJpaRepository;
import cl.banco_andino_quinchos.baq.Reserva.Repository.ReservaJpaRepository;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.UsuarioJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class QuinchoService {
    private final QuinchoJpaRepository quinchoRepository;
    private final BitacoraService bitacoraService;
    private final UsuarioJpaRepository usuarioRepository;
    private final ReservaJpaRepository reservaRepository;

    public QuinchoService(QuinchoJpaRepository quinchoRepository, BitacoraService bitacoraService, UsuarioJpaRepository usuarioRepository, ReservaJpaRepository reservaRepository) {
        this.quinchoRepository = quinchoRepository;
        this.bitacoraService = bitacoraService;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
    }


    public QuinchoJPA crear(QuinchoJPA quincho, Integer idAdmin) {

        QuinchoJPA guardado = quinchoRepository.save(quincho);

        UsuarioJPA admin = usuarioRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        bitacoraService.registrarAccion(
                "CREAR_QUINCHO",
                "Quincho " + guardado.getNombre() + " creado",
                null,
                admin
        );

        return guardado;
    }

    public List<QuinchoJPA> listarTodos() {
        return quinchoRepository.findAll();
    }

    public QuinchoJPA obtenerPorId(Long id) {
        return quinchoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quincho no encontrado con id: " + id));
    }

    public QuinchoJPA actualizar(Long id, QuinchoJPA datosActualizados) {
        QuinchoJPA existente = obtenerPorId(id);

        existente.setNombre(datosActualizados.getNombre());
        existente.setCapacidad(datosActualizados.getCapacidad());
        existente.setDisponibilidad(datosActualizados.getDisponibilidad());
        existente.setUbicacion(datosActualizados.getUbicacion());

        QuinchoJPA actualizado = quinchoRepository.save(existente);

        bitacoraService.registrarAccion(
                "MODIFICAR_QUINCHO",
                "Quincho " + actualizado.getNombre() + " actualizado",
                null,
                null
        );

        return actualizado;
    }

    public void cambiarEstado(Long id, boolean activo) {
        QuinchoJPA q = obtenerPorId(id);
        q.setDisponibilidad(activo);
        quinchoRepository.save(q);

        bitacoraService.registrarAccion(
                "CAMBIAR_ESTADO_QUINCHO",
                "Quincho " + q.getNombre() + " -> " + (activo ? "ACTIVO" : "INACTIVO"),
                null,
                null
        );
    }

    public List<QuinchoJPA> listarActivos() {
        return quinchoRepository.findAll()
                .stream()
                .filter(QuinchoJPA::getDisponibilidad)
                .toList();
    }

    @Transactional
    public void eliminar(Integer idQuincho, Integer idAdmin) {

        QuinchoJPA quincho = quinchoRepository.findById(idQuincho)
                .orElseThrow(() -> new RuntimeException("Quincho no encontrado"));

        UsuarioJPA admin = usuarioRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        bitacoraService.registrarAccion(
                "ELIMINAR_QUINCHO",
                "Eliminación del quincho '" + quincho.getNombre() + "' (ID: " + quincho.getId() + ")",
                null,
                admin
        );
        quinchoRepository.delete(quincho);
    }

    public boolean eliminarSiNoTieneReservas(Integer idQuincho) {
        boolean tieneReservas = reservaRepository.existsByQuinchoId(idQuincho);

        if (tieneReservas) {
            return false;
        }

        quinchoRepository.deleteById(idQuincho);
        return true;
    }
}