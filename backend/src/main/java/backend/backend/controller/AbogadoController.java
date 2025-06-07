package backend.backend.controller;

import backend.backend.dao.DaoAbogado;
import backend.backend.dao.DaoCliente;
import backend.backend.dao.DaoDocumento;
import backend.backend.dao.DaoEmail;
import backend.backend.entity.Abogado;
import backend.backend.entity.Cliente;
import backend.backend.entity.Documento;
import backend.backend.security.JWUtils;
import backend.backend.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/abogado")
@PreAuthorize("hasRole('ABOGADO')")
public class AbogadoController {
    @Autowired
    private DaoAbogado daoAbogado;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DaoCliente daoCliente;

    @Autowired
    private DaoDocumento daoDocumento;

    @Autowired
    private JWUtils jwUtils;

    @Autowired
    private DaoEmail daoEmail;

    @RequestMapping("/clientes")
    public ResponseEntity<?> getClientes(Authentication auth) {
        String dni = auth.getName();
        System.out.println(dni);
        Optional<Abogado> abogadopt = daoAbogado.findById_Dni(dni);
        if (abogadopt.isPresent()) {
            Abogado abogado = abogadopt.get();
            List<Cliente> clientes = daoCliente.findByAbogado(abogado);
            return ResponseEntity.ok(clientes);
        } else {
            return ResponseEntity.status(404).body("El abogado no existe");
        }
    }

    @GetMapping("/registro-cliente")
    public ResponseEntity<?> registroCliente(@RequestParam String correo, Authentication auth) {
        String dni = auth.getName();
        String token = jwUtils.generateToken(dni);
        String enlace = "http://localhost:5173/registro-cliente?token=" + token;

        try {
            String destino = correo;
            daoEmail.enviarEmail(destino, enlace);
            return ResponseEntity.ok("Email enviado a " + destino);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el email " + e.getMessage());
        }
    }

    @GetMapping("/documentos")
    public ResponseEntity<?> getDocumentosDeCliente(
            Authentication auth,
            @RequestParam String dniCliente) {

        // Obtener el dni del abogado autenticado desde el token
        String dniAbogado = auth.getName();
        System.out.println(dniAbogado);
        System.out.println(dniCliente);

        // Opcional: comprobar que el abogado existe (si quieres validar)
        if (!daoAbogado.existsById_Dni(dniAbogado)) {
            return ResponseEntity.status(404).body("Abogado no existe");
        }

        // Comprobar si el cliente está realmente asociado a este abogado
        boolean clienteAsociado = daoCliente.existsByDniAndAbogado_Id_Dni(dniCliente, dniAbogado);
        if (!clienteAsociado) {
            return ResponseEntity.status(404).body("Cliente no asociado a este abogado");
        }

        // Obtener los documentos filtrando por ambos DNIs
        List<Documento> documentos = daoDocumento.findAllByAbogado_Id_DniAndDniCliente_Dni(dniAbogado, dniCliente);
        return ResponseEntity.ok(documentos);
    }
}



