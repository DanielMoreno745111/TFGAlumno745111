package backend.backend.controller;


import backend.backend.dao.DaoDocumento;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
@PreAuthorize("hasRole('CLIENTE')")
public class ClienteController {

    @Autowired
    private DaoDocumento daoDocumento;

    @GetMapping("/documentos")
    public ResponseEntity<?> getDocumentos(Authentication auth){
        String dni = auth.getName();
        return ResponseEntity.ok(daoDocumento.findByDniCliente_Dni(dni));
    }
}
