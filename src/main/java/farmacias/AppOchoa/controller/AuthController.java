package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.dto.usuario.LoginDTO;
import farmacias.AppOchoa.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints de seguridad para obtener el Token JWT")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar Sesión", description = "Autentica al usuario y devuelve un token Bearer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, devuelve el Token"),
            @ApiResponse(responseCode = "400", description = "Faltan datos (usuario o contraseña vacíos)"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            String nombreUsuario = loginDto.getNombreUsuario();
            String contrasena = loginDto.getContrasena();

            if (nombreUsuario == null || contrasena == null || nombreUsuario.isBlank() || contrasena.isBlank()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Nombre de usuario y contraseña son obligatorios");
                return ResponseEntity.badRequest().body(error);
            }

            // Llamar al servicio de autenticación
            Map<String, Object> response = authService.login(nombreUsuario, contrasena);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}