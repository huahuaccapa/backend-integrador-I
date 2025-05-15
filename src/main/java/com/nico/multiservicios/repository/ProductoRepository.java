package com.nico.multiservicios.repository;

import com.nico.multiservicios.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
