package com.nico.multiservicios.repository;

import com.nico.multiservicios.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByProveedorId(Long proveedorId);
    List<Pedido> findByEstado(String estado);
}
