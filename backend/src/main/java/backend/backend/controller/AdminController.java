    package backend.backend.controller;


    import backend.backend.dao.*;
    import backend.backend.dto.ClienteDto;
    import backend.backend.entity.Cliente;
    import backend.backend.entity.Solicitud;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")

    public class AdminController {

        @Autowired
        private DaoAbogado daoAbogado;

        @Autowired
        private DaoCliente daoCliente;

        @Autowired
        private DaoDocumento daoDocumento;

        @Autowired
        private DaoSolicitud daoSolicitud;

        @Autowired
        private DaoEmail daoEmail;

        @GetMapping("/abogados")
        public ResponseEntity<?> getAbogados(){
            if(daoAbogado.findAll().size() > 0){
                return ResponseEntity.ok(daoAbogado.findAll());
            }
            else{
                return ResponseEntity.ok("No hay abogados registrados");
            }
        }

        @GetMapping("/clientesabogado")
        public ResponseEntity<?> obtenerClientesAbogado(@RequestParam String dni, @RequestParam String numeroColegiando){
            List<Cliente> clientes = daoCliente.findByAbogado_Id_DniAndAbogado_Id_NumeroColegiando(dni, numeroColegiando);
            List< ClienteDto> clientesDto = clientes.stream().map(c -> new ClienteDto(
                    c.getDni(),
                    c.getNombre(),
                    c.getTelefono(),
                    c.getCorreo()
            )).toList();
            if(clientes.size() > 0){
                return ResponseEntity.ok(clientesDto);
            }else{
                return ResponseEntity.ok("No hay clientes registrados para este abogado");
            }
        }

        @GetMapping("/solicitudes")
        public ResponseEntity<?> obtenerSolicitudes(){
            if(daoSolicitud.findAll().size() > 0){
                return ResponseEntity.ok(daoSolicitud.findAll());
            }
            else{
                return ResponseEntity.ok("No hay solicitudes registrados");
            }
        }

        @PutMapping("/aceptarsolicitud")
        public ResponseEntity<?> aceptarSolicitud(@RequestParam Long id){
            Optional<Solicitud> solicitudopt = daoSolicitud.findById(id);

            if(solicitudopt.isEmpty()){
                return ResponseEntity.status(404).body("La solicitud no existe");
            }

            Solicitud solicitudAceptada = solicitudopt.get();
            String mail = solicitudAceptada.getMail();

            String enlace = "http://localhost:5173/registro-abogado\n";

            try{
                daoEmail.enviarEmail(mail, enlace);
                daoSolicitud.delete(solicitudAceptada);
                return ResponseEntity.ok("Email enviado a "+mail);
            }catch(Exception e){
                return ResponseEntity.status(500).body("Error al enviar el email "+e.getMessage());
            }
        }

        @DeleteMapping("eliminar-solicitud")
        public ResponseEntity<?> eliminarSolicitud(@RequestParam Long id){
            if(!daoSolicitud.existsById(id)){
                return ResponseEntity.status(404).body("La solicitud no existe");
            }
            daoSolicitud.deleteById(id);
            return ResponseEntity.ok("Solicitud eliminada con éxito");
        }
    }
