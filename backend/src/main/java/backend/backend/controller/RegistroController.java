package backend.backend.controller;

import backend.backend.dao.DaoAbogado;
import backend.backend.dao.DaoCliente;
import backend.backend.dao.DaoSolicitud;
import backend.backend.entity.Abogado;
import backend.backend.entity.Cliente;
import backend.backend.entity.Solicitud;
import backend.backend.security.JWUtils;
import backend.backend.services.AbogadoService;
import backend.backend.services.ClienteService;
import backend.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private DaoAbogado daoAbogado;

    @Autowired
    private DaoCliente daoCliente;

    @Autowired
    private AbogadoService abogadoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DaoSolicitud daoSolicitud;

    @Autowired
    private JWUtils jwtUtils;

    @GetMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestParam String destino, @RequestParam String numeroColegiado){
        try{
            emailService.enviarEmail(destino, numeroColegiado);
            return ResponseEntity.ok("Email enviado a "+destino);
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error al enviar el email "+e.getMessage());
        }
    }

    @PostMapping("solicitud")
    public ResponseEntity<?> solicitud(@RequestBody Solicitud solicitud){
        Solicitud crearsolicitud = new Solicitud();
        crearsolicitud.setNumeroColegiado(solicitud.getNumeroColegiado());
        crearsolicitud.setMail(solicitud.getMail());
        crearsolicitud.setDni(solicitud.getDni());

        daoSolicitud.save(crearsolicitud);

        return ResponseEntity.ok("Solicitud enviada correctamente");
    }

    @RequestMapping("/registro-abogado")
    public ResponseEntity<?> registro(@RequestBody Abogado abogado){
        try{
            Abogado nuevoAbogado= abogadoService.save(abogado);
            return ResponseEntity.ok(nuevoAbogado);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body("El abogado ya existe "+e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error al registrar el abogado "+e.getMessage());
        }
    }

    @RequestMapping("/registro-cliente")
    public ResponseEntity<?> registroCliente(
            @RequestBody Cliente cliente,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        String dniAbogado = jwtUtils.getDniFromToken(token);

        Optional<Abogado> abogado = daoAbogado.findById_Dni(dniAbogado);
        if (abogado.isEmpty()) {
            return ResponseEntity.status(400).body("El abogado no existe");
        }
        cliente.setAbogado(abogado.get());
        Cliente newCliente = clienteService.save(cliente);
        return ResponseEntity.ok(newCliente);
    }


}
