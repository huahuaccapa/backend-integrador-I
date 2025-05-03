package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final List<User> users = new ArrayList<>();

    public AuthController() {
        users.add(new User("admin", "admin123", "ADMIN"));
        users.add(new User("empleado", "empleado123", "EMPLEADO"));
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        return users.stream()
            .filter(user -> user.getUsername().equals(loginRequest.getUsername()) && 
                          user.getPassword().equals(loginRequest.getPassword()))
            .findFirst()
            .map(User::getRole)
            .orElse("ERROR");
    }
}