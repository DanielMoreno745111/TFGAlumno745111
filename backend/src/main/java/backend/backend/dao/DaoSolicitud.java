package backend.backend.dao;

import backend.backend.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DaoSolicitud extends JpaRepository<Solicitud, Long> {

}
