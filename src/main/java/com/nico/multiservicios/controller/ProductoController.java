package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.Producto;
import com.nico.multiservicios.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        // Validación básica
        if (producto.getNombreProducto() == null || producto.getNombreProducto().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Establecer estado automáticamente según stock
        if (producto.getStock() == null || producto.getStock() <= 5) {
            producto.setEstado("BAJO");
        } else {
            producto.setEstado("OPTIMO");
        }

        Producto nuevoProducto = productoRepository.save(producto);
        return ResponseEntity.status(201).body(nuevoProducto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> listarProductoPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoActualizado) {

        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        productoActualizado.setId(id);
        
        // Actualizar estado según stock
        if (productoActualizado.getStock() <= 5) {
            productoActualizado.setEstado("BAJO");
        } else {
            productoActualizado.setEstado("OPTIMO");
        }

        Producto productoActualizadoDB = productoRepository.save(productoActualizado);
        return ResponseEntity.ok(productoActualizadoDB);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
