package com.minimarket.app.controlador;

import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthRestController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginREST(@RequestBody Map<String, String> credenciales) {
        Map<String, Object> response = new HashMap<>();
        
        //recibimos las credenciales del JSON
        String username = credenciales.get("username");
        String password = credenciales.get("password");

        
        Optional<Usuario> optUsuario = usuarioRepositorio.findByUsuario(username);

        //validacion
        if (optUsuario.isPresent() && passwordEncoder.matches(password, optUsuario.get().getPassword())) {
            
            Usuario usuario = optUsuario.get();
            
            response.put("mensaje", "Autenticación exitosa");
            response.put("usuario", usuario.getUsername());
            
            //iteramos y extraemos los nombres
            List<String> rolesDelUsuario = usuario.getRoles().stream()
                    .map(rol -> rol.getNombre())
                    .collect(Collectors.toList());

            response.put("roles", rolesDelUsuario);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("error", "Usuario o contraseña incorrectos");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); 
        }
    }
}