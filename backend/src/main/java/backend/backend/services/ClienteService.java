package backend.backend.services;

import backend.backend.dao.DaoCliente;
import backend.backend.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ClienteService {
    @Autowired
    private DaoCliente daoCliente;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Cliente> findByDni(String dni){
        return daoCliente.findByDni(dni);
    }

    public List<Cliente> obtenerClientesPorAbogado(String dni, String numeroColegiando){
        return daoCliente.findByAbogado_Id_DniAndAbogado_Id_NumeroColegiando(dni, numeroColegiando);
    }

    public Cliente save(Cliente cliente){
        if(daoCliente.findByDni(cliente.getDni()).isPresent()){
            throw new IllegalArgumentException("El cliente ya existe");
        }
        if(daoCliente.existsBycorreo(cliente.getCorreo())){
            throw new IllegalArgumentException("El correo ya existe");
        }
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        return daoCliente.save(cliente);
    }
}
