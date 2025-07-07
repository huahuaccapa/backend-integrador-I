package com.nico.multiservicios.repository;

import com.nico.multiservicios.model.Venta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "detalles",
            "cliente",
            "detalles.producto",
            "detalles.producto.imagenes"  // ✅ Necesario para evitar el error
    })
    List<Venta> findAll();


}