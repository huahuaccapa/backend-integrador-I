package com.nico.multiservicios.repository;

import com.nico.multiservicios.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
}