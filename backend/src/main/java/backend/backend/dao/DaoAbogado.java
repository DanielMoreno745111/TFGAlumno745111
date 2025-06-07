package backend.backend.dao;

import backend.backend.entity.Abogado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaoAbogado extends JpaRepository<Abogado, String> {
    Optional<Abogado> findById_Dni(String dni);
    boolean existsById_Dni(String dni);

    boolean existsBycorreo(String correo);
}
