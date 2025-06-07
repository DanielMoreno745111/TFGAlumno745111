package backend.backend.services;

import backend.backend.dao.DaoAdministrador;
import backend.backend.entity.Administrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AdministradorService {
    @Autowired
    private DaoAdministrador daoAdministrador;

    public Optional<Administrador> findByDni(String dni){
        return daoAdministrador.findByDni(dni);
    }
}
