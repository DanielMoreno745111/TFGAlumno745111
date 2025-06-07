package backend.backend.dao;

import backend.backend.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaoAdministrador extends JpaRepository<Administrador, String> {
    Optional<Administrador> findByDni(String dni);
}
