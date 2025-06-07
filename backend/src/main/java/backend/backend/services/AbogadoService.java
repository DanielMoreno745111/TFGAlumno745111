package backend.backend.services;

import backend.backend.dao.DaoAbogado;
import backend.backend.entity.Abogado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AbogadoService {
    @Autowired
    private DaoAbogado daoAbogado;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Abogado> findById_Dni(String dni){
        return daoAbogado.findById_Dni(dni);
    }

    public Abogado save(Abogado abogado){
        if(daoAbogado.existsById_Dni(abogado.getId().getDni())){
            throw new IllegalArgumentException("El abogado ya existe");
        }
        if(daoAbogado.existsBycorreo(abogado.getCorreo())){
            throw new IllegalArgumentException("El correo ya existe");
        }
        abogado.setContrasena(passwordEncoder.encode(abogado.getContrasena()));
        return daoAbogado.save(abogado);
    }
}
