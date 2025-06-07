package backend.backend.services;

import backend.backend.dao.DaoAbogado;
import backend.backend.dao.DaoAdministrador;
import backend.backend.dao.DaoCliente;
import backend.backend.dao.UserDetailsInterface;
import backend.backend.entity.Abogado;
import backend.backend.entity.Administrador;
import backend.backend.entity.Cliente;
import backend.backend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsInterfaceService implements UserDetailsService {

    @Autowired
    private DaoAbogado daoAbogado;

    @Autowired
    private DaoCliente daoCliente;

    @Autowired
    private DaoAdministrador daoAdministrador;

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {

        Optional<Cliente> clienteOpt = daoCliente.findByDni(dni);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            return new CustomUserDetails(cliente.getDni(), cliente.getContrasena(), "CLIENTE");
        }

        Optional<Abogado> abogadoOpt = daoAbogado.findById_Dni(dni);
        if (abogadoOpt.isPresent()) {
            Abogado abogado = abogadoOpt.get();
            return new CustomUserDetails(abogado.getId().getDni(), abogado.getContrasena(), "ABOGADO");
        }

        Optional<Administrador> adminOpt = daoAdministrador.findByDni(dni);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            return new CustomUserDetails(admin.getDni(), admin.getContrasena(), "ADMIN");
        }

        throw new UsernameNotFoundException("Usuario no encontrado con DNI: " + dni);
    }

}
