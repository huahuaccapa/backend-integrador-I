package com.nico.multiservicios.repository;

import com.nico.multiservicios.model.Producto;
import com.nico.multiservicios.model.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByStockGreaterThan(int stock);
    List<Producto> findByNombreProductoContaining(String nombre);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre); // Añade este método
    List<Producto> findByEstado(String estado);
    List<Producto> findByTipoProducto(TipoProducto tipoProducto);
}   