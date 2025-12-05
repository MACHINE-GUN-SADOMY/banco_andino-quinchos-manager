package cl.banco_andino_quinchos.baq.Reserva.Repository;

import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import cl.banco_andino_quinchos.baq.Reserva.Repository.JPA.ReservaJPA;
import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaJpaRepository extends JpaRepository<ReservaJPA, Integer>{
    @Query("""
  SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
  FROM ReservaJPA r
  WHERE r.id = :quinchoId
    AND r.estado IN ('PENDIENTE','APROBADA')
    AND (r.fechaInicio < :fin AND r.fechaFin > :inicio)
    """)
    boolean existsOverlap(Integer quinchoId, LocalDateTime inicio, LocalDateTime fin);


    List<ReservaJPA> findByUsuario(UsuarioJPA usuario);
    List<ReservaJPA> findByQuincho(QuinchoJPA quincho);
    boolean existsByQuinchoId(Integer id);
}
