package backend.backend.services;

import backend.backend.dao.DaoSolicitud;
import backend.backend.entity.Solicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    private DaoSolicitud daoSolicitud;

    public List<Solicitud> listarTodo() {
        return daoSolicitud.findAll();
    }

    public Optional<Solicitud> buscarId(Long id) {
        return daoSolicitud.findById(id);
    }

    public boolean eliminarporId(Long id) {
        if(daoSolicitud.existsById(id)){
            daoSolicitud.deleteById(id);
            return true;
        }
        return false;
    }
}
