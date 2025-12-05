package cl.banco_andino_quinchos.baq.Reserva.Service;

import cl.banco_andino_quinchos.baq.Bitacora.Service.BitacoraService;
import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Quincho.Repository.QuinchoJpaRepository;
import cl.banco_andino_quinchos.baq.Reserva.Repository.JPA.ReservaJPA;
import cl.banco_andino_quinchos.baq.Reserva.Repository.ReservaJpaRepository;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.UsuarioJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservaService {

    private final ReservaJpaRepository reservaRepository;
    private final UsuarioJpaRepository usuarioRepository;
    private final QuinchoJpaRepository quinchoRepository;
    private final BitacoraService bitacoraService;

    public ReservaService(ReservaJpaRepository reservaRepository,
                          UsuarioJpaRepository usuarioRepository,
                          QuinchoJpaRepository quinchoRepository,
                          BitacoraService bitacoraService) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.quinchoRepository = quinchoRepository;
        this.bitacoraService = bitacoraService;
    }

    public ReservaJPA crearReserva(Integer idUsuario,
                                   Integer idQuincho,
                                   LocalDateTime fechaInicio,
                                   LocalDateTime fechaFin,
                                   String motivo) {

        LocalDateTime ahora = LocalDateTime.now();

        if (fechaInicio.isBefore(ahora)) {
            throw new RuntimeException("La fecha de inicio no puede ser anterior al momento actual.");
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            throw new RuntimeException("La fecha de término debe ser posterior a la fecha de inicio.");
        }

        UsuarioJPA usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        QuinchoJPA quincho = quinchoRepository.findById(idQuincho)
                .orElseThrow(() -> new RuntimeException("Quincho no encontrado"));

        if (!Boolean.TRUE.equals(quincho.getDisponibilidad())) {
            throw new RuntimeException("El quincho no se encuentra disponible");
        }

        ReservaJPA reserva = ReservaJPA.builder()
                .usuario(usuario)
                .quincho(quincho)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .estado("PENDIENTE")
                .aprobado(false)
                .motivo(motivo)
                .build();

        ReservaJPA guardada = reservaRepository.save(reserva);

        bitacoraService.registrarAccion(
                "CREAR_RESERVA",
                "Reserva " + guardada.getId() + " creada para quincho " + quincho.getNombre(),
                null,
                usuario
        );

        quincho.setDisponibilidad(false);
        quinchoRepository.save(quincho);

        return guardada;
    }

    public List<ReservaJPA> listarTodas() {
        return reservaRepository.findAll();
    }

    public ReservaJPA obtenerPorId(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    public List<ReservaJPA> listarPorUsuario(Integer idUsuario) {
        UsuarioJPA usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return reservaRepository.findByUsuario(usuario);
    }

    public List<ReservaJPA> listarPorQuincho(Integer idQuincho) {
        QuinchoJPA quincho = quinchoRepository.findById(idQuincho)
                .orElseThrow(() -> new RuntimeException("Quincho no encontrado"));
        return reservaRepository.findByQuincho(quincho);
    }

    public ReservaJPA cambiarEstado(Integer idReserva,
                                    String nuevoEstado,
                                    Boolean aprobado,
                                    Integer idAdmin) {

        ReservaJPA reserva = obtenerPorId(idReserva);

        String estadoAnterior = reserva.getEstado();

        QuinchoJPA quincho = reserva.getQuincho();

        reserva.setEstado(nuevoEstado);
        if (aprobado != null) {
            reserva.setAprobado(aprobado);
        }

        if ("RECHAZADA".equalsIgnoreCase(nuevoEstado)) {
            quincho.setDisponibilidad(true);
            quinchoRepository.save(quincho);
        } else if ("APROBADA".equalsIgnoreCase(nuevoEstado)
                || "PENDIENTE".equalsIgnoreCase(nuevoEstado)) {
            quincho.setDisponibilidad(false);
            quinchoRepository.save(quincho);
        }

        ReservaJPA guardada = reservaRepository.save(reserva);

        UsuarioJPA admin = usuarioRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        bitacoraService.registrarAccion(
                "CAMBIO_ESTADO_RESERVA",
                "Reserva " + idReserva + ": " + estadoAnterior + " → " + nuevoEstado,
                null,
                admin
        );

        return guardada;
    }


    @Transactional
    public ReservaJPA cancelarPorUsuario(Integer idReserva, Integer idUsuario) {

        ReservaJPA reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getUsuario().getId().equals(idUsuario)) {
            throw new RuntimeException("La reserva no pertenece a este usuario.");
        }

        if (!"PENDIENTE".equals(reserva.getEstado()) &&
                !"APROBADA".equals(reserva.getEstado())) {
            throw new RuntimeException("No se puede cancelar una reserva en estado " + reserva.getEstado());
        }

        reserva.setEstado("CANCELADA");
        reserva.setAprobado(false);

        QuinchoJPA quincho = reserva.getQuincho();
        quincho.setDisponibilidad(true);
        quinchoRepository.save(quincho);

        ReservaJPA guardada = reservaRepository.save(reserva);

        bitacoraService.registrarAccion(
                "CANCELAR_RESERVA",
                "Reserva " + idReserva + " cancelada por el usuario",
                null,
                reserva.getUsuario()
        );

        return guardada;
    }
}
