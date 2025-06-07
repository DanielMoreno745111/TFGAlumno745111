package backend.backend.dao;
import backend.backend.entity.Abogado;
import backend.backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DaoCliente extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByDni(String dni);
    List<Cliente> findByAbogado_Id_DniAndAbogado_Id_NumeroColegiando(String dni, String numeroColegiando);
    List<Cliente> findByAbogado(Abogado abogado);
    boolean existsByDniAndAbogado_Id_Dni(String dniCliente, String dniAbogado);
    boolean existsBycorreo(String correo);

    Optional<Cliente> findByDniAndAbogado(String dniCliente, Abogado abogado);


}
