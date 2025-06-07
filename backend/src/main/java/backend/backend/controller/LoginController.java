    package backend.backend.controller;

    import backend.backend.dto.LoginRequest;
    import backend.backend.entity.Abogado;
    import backend.backend.entity.Administrador;
    import backend.backend.entity.Cliente;
    import backend.backend.security.JWUtils;
    import backend.backend.services.AbogadoService;
    import backend.backend.services.AdministradorService;
    import backend.backend.services.ClienteService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.Map;
    import java.util.Optional;

    @RestController
    @RequestMapping("/login")
    public class LoginController {
        @Autowired
        private AdministradorService administradorService;
        @Autowired
        private AbogadoService abogadoService;
        @Autowired
        private ClienteService clienteService;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private JWUtils jwtUtils;
        @PostMapping
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
            String dni = loginRequest.getDni();
            String contrasenalimpia = loginRequest.getContrasena();
            String rol = loginRequest.getRol(); // "ADMIN", "ABOGADO", "CLIENTE"

            switch (rol.toUpperCase()) {
                case "ADMIN":
                    Optional<Administrador> admin = administradorService.findByDni(dni);
                    if (admin.isPresent() && passwordEncoder.matches(contrasenalimpia, admin.get().getContrasena())) {
                        String token = jwtUtils.generateToken(dni, rol);
                        return ResponseEntity.ok(Map.of("token", token));
                    }
                    break;
                case "ABOGADO":
                    Optional<Abogado> abogado = abogadoService.findById_Dni(dni);
                    if (abogado.isPresent() && passwordEncoder.matches(contrasenalimpia, abogado.get().getContrasena())) {
                        String token = jwtUtils.generateToken(dni, rol);
                        return ResponseEntity.ok(Map.of("token", token));
                    }
                    break;
                case "CLIENTE":
                    Optional<Cliente> cliente = clienteService.findByDni(dni);
                    if (cliente.isPresent() && passwordEncoder.matches(contrasenalimpia, cliente.get().getContrasena())) {
                        String token = jwtUtils.generateToken(dni, rol);
                        return ResponseEntity.ok(Map.of("token", token));
                    }
                    break;
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }

    }
