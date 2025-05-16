package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.Producto;
import com.nico.multiservicios.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/productos")

public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> listarProductos()
    {
        return productoRepository.findAll();
    }

    @PostMapping
    public Producto crerProducto( @RequestBody Producto producto)
    {
        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> listarProductosPorId(@PathVariable Long id)
    {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> editarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoActualizado) {

        return productoRepository.findById(id)
                .map(productoExistente -> {
                    productoExistente.setNombreProducto(productoActualizado.getNombreProducto());
                    productoExistente.setPrecio(productoActualizado.getPrecio());
                    productoExistente.setStock(productoActualizado.getStock());
                    productoExistente.setEstadoStock(productoActualizado.getEstadoStock());
                    // Actualizar otros campos según tu modelo

                    Producto productoGuardado = productoRepository.save(productoExistente);
                    return ResponseEntity.ok(productoGuardado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
