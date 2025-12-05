package cl.banco_andino_quinchos.baq.Usuario.Repository;


import cl.banco_andino_quinchos.baq.Usuario.Repository.JPA.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJPA, Integer> {
    Optional<UsuarioJPA> findByCorreoIgnoreCase(String correo);
    Optional<UsuarioJPA> findByCorreoAndPassword(String correo, String password);
    Optional<UsuarioJPA> findById(Integer id);
    Optional<UsuarioJPA> findByCorreo(String correo);
}
