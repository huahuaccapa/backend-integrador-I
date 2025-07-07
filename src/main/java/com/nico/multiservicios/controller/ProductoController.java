package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.Producto;
import com.nico.multiservicios.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173") // ajusta si es necesario
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // Crear un nuevo producto
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        System.out.println("Producto recibido:");
        System.out.println(producto);

        return productoRepository.save(producto);
    }

    // Obtener todos los productos
    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public Producto obtenerProducto(@PathVariable Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }

    // Editar producto
    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        return productoRepository.findById(id).map(producto -> {
            producto.setCodigo(productoActualizado.getCodigo());
            producto.setNombreProducto(productoActualizado.getNombreProducto());
            producto.setCategoria(productoActualizado.getCategoria());
            producto.setStock(productoActualizado.getStock());
            producto.setStockMinimo(productoActualizado.getStockMinimo());
            producto.setStockMaximo(productoActualizado.getStockMaximo());
            producto.setPrecioCompra(productoActualizado.getPrecioCompra());
            producto.setPrecioVenta(productoActualizado.getPrecioVenta());



            //producto.setMarca(productoActualizado.getMarca());
            //producto.setEstado(productoActualizado.getEstado());
            //producto.setDescripcion(productoActualizado.getDescripcion());

            producto.setImagenes(productoActualizado.getImagenes());

            return productoRepository.save(producto);
        }).orElse(null);
    }

    @GetMapping("/stock-bajo")
    public List<Producto> obtenerProductosConStockBajo() {
        List<Producto> todosLosProductos = productoRepository.findAll();
        return todosLosProductos.stream()
                .filter(producto -> producto.getStock() <= producto.getStockMinimo())
                .collect(Collectors.toList());
    }
}
