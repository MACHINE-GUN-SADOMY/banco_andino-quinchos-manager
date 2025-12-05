package cl.banco_andino_quinchos.baq.Quincho.Repository;


import cl.banco_andino_quinchos.baq.Quincho.Repository.JPA.QuinchoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuinchoJpaRepository extends JpaRepository<QuinchoJPA, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);
    List<QuinchoJPA> findByCapacidadGreaterThanEqual(Integer capacidad);
    Optional<QuinchoJPA> findById(Long id);
}
