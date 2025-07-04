package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.User;
import com.nico.multiservicios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        initializeDefaultUsers();
    }

    private void initializeDefaultUsers() {
        User admin = userRepository.findByUsername("admin");
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");  // No encriptamos la contraseña para el admin
            admin.setRole("ADMIN");
            admin.setEmail("admin@multiservicios.com");
            userRepository.save(admin);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            User user = userRepository.findByUsername(loginRequest.getUsername());

            if (user == null) {
                return ResponseEntity.status(401).body("Usuario no encontrado");
            }

            // Verificar contraseña
            boolean passwordMatches = false;
            
            // Para el admin, comparar directamente (sin encriptación)
            if (user.getUsername().equals("admin")) {
                passwordMatches = loginRequest.getPassword().equals(user.getPassword());
            } else {
                // Para usuarios normales, usar BCrypt
                passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            }

            if (!passwordMatches) {
                return ResponseEntity.status(401).body("Contraseña incorrecta");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el servidor");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        try {
            if (newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de usuario es requerido");
            }

            if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña es requerida");
            }

            if (userRepository.existsById(newUser.getUsername())) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
            }

            // Encriptar la contraseña antes de guardarla
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

            // Forzar rol EMPLEADO para todos los nuevos registros
            newUser.setRole("EMPLEADO");

            userRepository.save(newUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("username", newUser.getUsername());
            response.put("role", newUser.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar usuario");
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, String> profile = new HashMap<>();
            profile.put("username", user.getUsername());
            profile.put("email", user.getEmail());
            profile.put("role", user.getRole());
            profile.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "N/A");

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener perfil");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            User user = userRepository.findByEmail(email);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Se ha enviado un enlace de recuperación a tu correo");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }
}