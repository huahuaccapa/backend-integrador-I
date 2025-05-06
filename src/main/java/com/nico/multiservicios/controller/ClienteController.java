package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    private static final String URL = "jdbc:mysql://localhost:3306/multiservicios?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "250520";

    private Connection conectar() throws SQLException {
        System.out.println("Conectando a la base de datos...");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Conexión exitosa!");
        return conn;
    }

    // ✅ Listar clientes (Actualizado)
    @GetMapping("/clientes")
    public ResponseEntity<?> listarClientes() {
        System.out.println("Recibida solicitud para listar clientes");
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre, apellidos, email, telefono, identificacion, fecha_registro FROM clientes";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Ejecutando consulta SQL: " + sql);
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setIdentificacion(rs.getString("identificacion"));
                cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                clientes.add(cliente);
            }

            System.out.println("Clientes encontrados: " + clientes.size());
            return ResponseEntity.ok(clientes);

        } catch (SQLException e) {
            System.err.println("Error al listar clientes:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener clientes: " + e.getMessage());
        }
    }

    //  Crear cliente 
    @PostMapping("/clientes")
    public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
        System.out.println("Recibida solicitud para crear cliente: " + cliente);
        
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El email es obligatorio");
        }

        String sql = "INSERT INTO clientes (nombre, apellidos, email, telefono, identificacion, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellidos() != null ? cliente.getApellidos() : "");
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono() != null ? cliente.getTelefono() : "");
            stmt.setString(5, cliente.getIdentificacion() != null ? cliente.getIdentificacion() : "");
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(6, now);

            System.out.println("Ejecutando inserción SQL: " + stmt);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No se pudo crear el cliente");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getLong(1));
                    cliente.setFechaRegistro(now);
                } else {
                    throw new SQLException("No se obtuvo el ID generado");
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);

        } catch (SQLException e) {
            System.err.println("Error SQL al crear cliente:");
            e.printStackTrace();

            if (e.getMessage().contains("Duplicate entry")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe un cliente con ese email");
            } else if (e.getMessage().contains("Data too long")) {
                return ResponseEntity.badRequest()
                        .body("Algunos datos exceden el tamaño permitido");
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear cliente: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al crear cliente:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al crear cliente");
        }
    }
    // Agrega este método al final de tu ClienteController.java

// ✅ Actualizar cliente (Nuevo método)
@PutMapping("/clientes/{id}")
public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
    System.out.println("Recibida solicitud para actualizar cliente ID: " + id);
    
    // Validaciones básicas
    if (clienteActualizado.getNombre() == null || clienteActualizado.getNombre().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre es obligatorio");
    }
    if (clienteActualizado.getEmail() == null || clienteActualizado.getEmail().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("El email es obligatorio");
    }

    String sql = "UPDATE clientes SET nombre = ?, apellidos = ?, email = ?, telefono = ?, identificacion = ? WHERE id = ?";

    try (Connection conn = conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, clienteActualizado.getNombre());
        stmt.setString(2, clienteActualizado.getApellidos() != null ? clienteActualizado.getApellidos() : "");
        stmt.setString(3, clienteActualizado.getEmail());
        stmt.setString(4, clienteActualizado.getTelefono() != null ? clienteActualizado.getTelefono() : "");
        stmt.setString(5, clienteActualizado.getIdentificacion() != null ? clienteActualizado.getIdentificacion() : "");
        stmt.setLong(6, id);

        System.out.println("Ejecutando actualización SQL: " + stmt);
        int filasAfectadas = stmt.executeUpdate();

        if (filasAfectadas > 0) {
            // Obtener el cliente actualizado para devolverlo
            String sqlSelect = "SELECT * FROM clientes WHERE id = ?";
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                stmtSelect.setLong(1, id);
                ResultSet rs = stmtSelect.executeQuery();
                
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getLong("id"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellidos(rs.getString("apellidos"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setIdentificacion(rs.getString("identificacion"));
                    cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    return ResponseEntity.ok(cliente);
                }
            }
            return ResponseEntity.ok(clienteActualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }

    } catch (SQLException e) {
        System.err.println("Error al actualizar cliente:");
        e.printStackTrace();

        if (e.getMessage().contains("Duplicate entry")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un cliente con ese email");
        } else if (e.getMessage().contains("Data too long")) {
            return ResponseEntity.badRequest()
                    .body("Algunos datos exceden el tamaño permitido");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar cliente: " + e.getMessage());
    }
}

    //  Eliminar cliente 
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        System.out.println("Recibida solicitud para eliminar cliente ID: " + id);
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            System.out.println("Ejecutando eliminación SQL: " + stmt);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                return ResponseEntity.ok("Cliente eliminado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar cliente: " + e.getMessage());
        }
    }
}