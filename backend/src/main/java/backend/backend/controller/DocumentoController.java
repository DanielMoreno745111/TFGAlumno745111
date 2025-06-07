package backend.backend.controller;

import backend.backend.dao.DaoAbogado;
import backend.backend.dao.DaoCliente;
import backend.backend.dao.DaoDocumento;
import backend.backend.entity.Abogado;
import backend.backend.entity.Cliente;
import backend.backend.entity.Documento;
import backend.backend.security.JWUtils;
import backend.backend.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    @Autowired
    JWUtils jwtUtils;

    @Autowired
    DocumentoService documentoService;
    @Autowired
    private DaoAbogado daoAbogado;
    @Autowired
    private DaoCliente daoCliente;
    @Autowired
    private DaoDocumento daoDocumento;

    @PostMapping("subida-cliente")
    @PreAuthorize("hasRole('CLIENTE')")

    public ResponseEntity<String> subirDocumentoCliente(@RequestParam MultipartFile file, Authentication auth){

        String dni = auth.getName();
        documentoService.subirdocumentoCliente(dni, file);
        return ResponseEntity.ok("Se subió el documento");
    }

    @PostMapping("/subida-abogado/{dniCliente}")
    @PreAuthorize("hasRole('ABOGADO')")
    public ResponseEntity<String> subirDocumentoComoAbogado(@PathVariable String dniCliente,
                                                            @RequestParam MultipartFile archivo, Authentication auth) {
        String dniAbogado = auth.getName();

        Optional<Abogado> abogados = daoAbogado.findById_Dni(dniAbogado);

        if (abogados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Abogado no encontrado");
        }

        Abogado abogado = abogados.get();
        Cliente cliente = daoCliente.findById(dniCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getAbogado().getId().equals(abogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El cliente no pertenece a este abogado");
        }

        Documento documento = new Documento();
        documento.setNombre(archivo.getOriginalFilename());
        documento.setUrl(documentoService.guardarArchivo(archivo));
        documento.setDniCliente(cliente);
        documento.setAbogado(abogado);

        daoDocumento.save(documento);

        return ResponseEntity.ok("Documento subido correctamente");
    }

    @GetMapping("/descargar/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ABOGADO')")
    public ResponseEntity<?> descargarDocumento(@PathVariable String id, Authentication auth) {
        Optional<Documento> optionalDocumento = daoDocumento.findById(id);

        if (optionalDocumento.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado");
        }

        Documento documento = optionalDocumento.get();
        String usuarioDni = auth.getName();

        boolean esCliente = documento.getDniCliente().getDni().equals(usuarioDni);
        boolean esAbogado = documento.getAbogado().getId().getDni().equals(usuarioDni);

        if (!esCliente && !esAbogado) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a este documento");
        }

        java.nio.file.Path filePath = java.nio.file.Paths.get(documento.getUrl());

        if (!java.nio.file.Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado en el servidor");
        }

        try {
            byte[] archivoBytes = java.nio.file.Files.readAllBytes(filePath);
            String nombreArchivo = filePath.getFileName().toString();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"")
                    .body(archivoBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al leer el archivo");
        }
    }

    @PostMapping("/presentado/{id}")
    public ResponseEntity<Documento> suma15dias(@PathVariable String id) {
        LocalDate fecha = LocalDate.now().plusDays(15);
        Documento documento = documentoService.encontrarPorId(id);
        documento.setFechaRespuesta(fecha);
        daoDocumento.save(documento);
        return ResponseEntity.ok(documento);


    }



}
