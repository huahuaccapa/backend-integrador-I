package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // CORREGIDO: puerto 5173
public class AuthController {

    private List<User> users = new ArrayList<>();

    public AuthController() {
        users.add(new User("admin", "admin123", "ADMIN"));
        users.add(new User("empleado", "empleado123", "EMPLEADO"));
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        for (User user : users) {
            if (user.getUsername().equals(loginRequest.getUsername()) && 
                user.getPassword().equals(loginRequest.getPassword())) {
                return user.getRole(); // Devuelve el rol como respuesta
            }
        }
        return "ERROR";
    }
}
