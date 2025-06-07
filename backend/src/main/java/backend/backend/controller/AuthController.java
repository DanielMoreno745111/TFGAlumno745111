package backend.backend.controller;

import backend.backend.dao.DaoAdministrador;
import backend.backend.dao.DaoAbogado;
import backend.backend.dao.DaoCliente;
import backend.backend.entity.Abogado;
import backend.backend.entity.Administrador;
import backend.backend.entity.Cliente;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final DaoCliente daoCliente;
    private final DaoAbogado daoAbogado;
    private final DaoAdministrador daoAdministrador;
    private final PasswordEncoder passwordEncoder;

    public AuthController(DaoCliente daoCliente, DaoAbogado daoAbogado, DaoAdministrador daoAdministrador, PasswordEncoder passwordEncoder) {
        this.daoCliente = daoCliente;
        this.daoAbogado = daoAbogado;
        this.daoAdministrador = daoAdministrador;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registerCliente(@RequestBody Cliente cliente) {
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        daoCliente.save(cliente);
        return ResponseEntity.ok("Cliente registrado con éxito");
    }

    @PostMapping("/register/abogado")
    public ResponseEntity<?> registerAbogado(@RequestBody Abogado abogado) {
        abogado.setContrasena(passwordEncoder.encode(abogado.getContrasena()));
        daoAbogado.save(abogado);
        return ResponseEntity.ok("Abogado registrado con éxito");
    }

    @PostMapping("/register/administrador")
    public ResponseEntity<?> registerAdministrador(@RequestBody Administrador administrador) {
        administrador.setContrasena(passwordEncoder.encode(administrador.getContrasena()));
        daoAdministrador.save(administrador);
        return ResponseEntity.ok("Administrador registrado con éxito");
    }
}
