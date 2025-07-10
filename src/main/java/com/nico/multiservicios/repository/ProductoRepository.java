package com.nico.multiservicios.repository;

import com.nico.multiservicios.model.Producto;
import com.nico.multiservicios.model.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByStockGreaterThan(int stock);
    List<Producto> findByNombreProductoContaining(String nombre);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre); // Añade este método
    List<Producto> findByEstado(String estado);

    List<Producto> findByStockLessThanEqual(Integer stock);
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.proveedor")
    List<Producto> findAllWithProveedor();

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.proveedor WHERE p.id = :id")
    Optional<Producto> findByIdWithProveedor(@Param("id") Long id);
}

