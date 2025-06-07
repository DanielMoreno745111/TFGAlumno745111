package backend.backend.dao;

import backend.backend.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaoDocumento extends JpaRepository<Documento, String> {
    List<Documento> findByDniCliente_Dni(String dni);
    List<Documento> findAllByAbogado_Id_DniAndDniCliente_Dni(String dniAbogado, String dniCliente);



}
