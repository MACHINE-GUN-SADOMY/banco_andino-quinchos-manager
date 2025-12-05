package cl.banco_andino_quinchos.baq.Bitacora.Service;

import cl.banco_andino_quinchos.baq.Bitacora.Repository.BitacoraJpaRepository;
import cl.banco_andino_quinchos.baq.Bitacora.Repository.JPA.BitacoraJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.UsuarioJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BitacoraService {
    private final BitacoraJpaRepository bitacoraRepository;

    public BitacoraService(BitacoraJpaRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }


    public BitacoraJPA registrarAccion(String tipoAccion,
                                       String accion,
                                       String detalle,
                                       UsuarioJPA usuario) {

        if (usuario == null) {
            return null;
        }

        BitacoraJPA b = new BitacoraJPA();

        String mensaje = tipoAccion;
        if (detalle != null && !detalle.isBlank()) {
            mensaje += " - " + detalle;
        }

        b.setAccion(accion);
        b.setDetalle(detalle);
        b.setUsuario(usuario);
        b.setFecha(LocalDateTime.now());

        return bitacoraRepository.save(b);
    }

    public List<BitacoraJPA> listarTodo() {
        return bitacoraRepository.findAll();
    }

    public List<BitacoraJPA> listarPorUsuario(Integer idUsuario) {
        return bitacoraRepository.findByUsuario_Id(idUsuario);
    }
}
