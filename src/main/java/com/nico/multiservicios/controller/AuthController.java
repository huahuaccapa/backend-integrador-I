package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.User;
import com.nico.multiservicios.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        // Datos iniciales (solo para desarrollo)
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", "admin123", "ADMIN"));
            userRepository.save(new User("empleado", "empleado123", "EMPLEADO"));
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        User user = userRepository.findByUsernameAndPassword(
            loginRequest.getUsername(), 
            loginRequest.getPassword()
        );
        return user != null ? user.getRole() : "ERROR";
    }
}