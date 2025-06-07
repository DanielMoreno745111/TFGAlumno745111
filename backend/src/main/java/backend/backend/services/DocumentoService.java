package backend.backend.services;

import backend.backend.dao.DaoCliente;
import backend.backend.dao.DaoDocumento;
import backend.backend.entity.Abogado;
import backend.backend.entity.Cliente;
import backend.backend.entity.Documento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class DocumentoService {

    @Autowired
    private DaoDocumento daoDocumento;

    @Autowired
    private DaoCliente daoCliente;

    public void subirdocumentoCliente(String dniCliente, MultipartFile file) {
        Cliente cliente = daoCliente.findById(dniCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Abogado abogado = cliente.getAbogado(); // Ya viene desde la BD

        Documento doc = new Documento();
        doc.setNombre(file.getOriginalFilename());
        doc.setUrl(guardarArchivo(file));
        doc.setDniCliente(cliente);
        doc.setFechaEntrega(LocalDate.now());
        doc.setAbogado(abogado);

        daoDocumento.save(doc);
    }

    public String guardarArchivo(MultipartFile file){
        try {
            String nombre = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path ruta = Paths.get("C:\\Users\\moren\\Downloads\\DescargaFCT" + nombre);
            Files.copy(file.getInputStream(), ruta);

            return ruta.toString();

        }catch(IOException e){
            throw new RuntimeException("Error al subir el archivo "+e.getMessage());
        }
    }

    public Documento encontrarPorId(String id) {
        if(!daoDocumento.findById(id).isPresent()){
            throw new RuntimeException("Documento no encontrado");
        }
        return daoDocumento.findById(id).get();
    }
}
