package com.nico.multiservicios.controller;

import com.nico.multiservicios.model.DetallePedido;
import com.nico.multiservicios.model.Pedido;
import com.nico.multiservicios.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "http://localhost:5173")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // CREATE - Crear nuevo pedido
    @PostMapping
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        if (pedido.getDetallePedido() != null) {
            for (DetallePedido detalle : pedido.getDetallePedido()) {
                detalle.setPedido(pedido); // ASOCIACIÓN BIDIRECCIONAL NECESARIA
            }
        }
        return pedidoRepository.save(pedido);
    }

    // READ ALL - Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodosPedidos() {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // READ ONE - Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - Actualizar un pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(
            @PathVariable Long id,
            @RequestBody Pedido pedidoActualizado) {

        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setProveedorId(pedidoActualizado.getProveedorId());
                    pedido.setEstado(pedidoActualizado.getEstado());
                    pedido.setMetodoPago(pedidoActualizado.getMetodoPago());
                    pedido.setFechaEntrega(pedidoActualizado.getFechaEntrega());
                    pedido.setTotal(pedidoActualizado.getTotal());

                    // Actualizar detalles si es necesario
                    if(pedidoActualizado.getDetallePedido() != null) {
                        pedido.getDetallePedido().clear();
                        pedido.getDetallePedido().addAll(pedidoActualizado.getDetallePedido());
                        pedido.getDetallePedido().forEach(d -> d.setPedido(pedido));
                    }

                    Pedido pedidoActualizadoDB = pedidoRepository.save(pedido);
                    return ResponseEntity.ok(pedidoActualizadoDB);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ENDPOINTS ADICIONALES

    // Obtener pedidos por proveedor
    @GetMapping("/proveedor/{proveedorId}")
    public List<Pedido> obtenerPedidosPorProveedor(@PathVariable Long proveedorId) {
        return pedidoRepository.findByProveedorId(proveedorId);
    }

    // Obtener pedidos por estado
    @GetMapping("/estado/{estado}")
    public List<Pedido> obtenerPedidosPorEstado(@PathVariable String estado) {
        return pedidoRepository.findByEstado(estado);
    }
}