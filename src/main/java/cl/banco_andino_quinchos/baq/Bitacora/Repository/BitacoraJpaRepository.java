package cl.banco_andino_quinchos.baq.Bitacora.Repository;

import cl.banco_andino_quinchos.baq.Bitacora.Repository.JPA.BitacoraJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BitacoraJpaRepository extends JpaRepository<BitacoraJPA, Integer> {
    List<BitacoraJPA> findByUsuarioId(Integer usuario);
    List<BitacoraJPA> findByUsuario_Id(Integer idUsuario);
}
